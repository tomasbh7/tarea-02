package com.example.tarea_02.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CarritoDAO {

    private static final String TAG = "CarritoDAO";

    private final DatabaseHelper dbHelper;

    public CarritoDAO(Context context) {
        Log.d(TAG, "CarritoDAO: Inicializando DAO");
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Agrega un producto al carrito. Si ya existe, aumenta su cantidad.
     */
    public void agregarProducto(String producto) {
        Log.d(TAG, "agregarProducto: Producto recibido -> " + producto);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT cantidad FROM carrito WHERE producto = ?",
                new String[]{producto}
        );

        if (cursor.moveToFirst()) {
            int cantidadActual = cursor.getInt(0);

            Log.d(TAG, "agregarProducto: Producto ya existe. Cantidad actual = " + cantidadActual);

            ContentValues values = new ContentValues();
            values.put("cantidad", cantidadActual + 1);

            db.update("carrito", values, "producto=?", new String[]{producto});
            Log.d(TAG, "agregarProducto: Cantidad actualizada a " + (cantidadActual + 1));

        } else {
            Log.d(TAG, "agregarProducto: Producto no existe. Insertando nuevo...");

            ContentValues values = new ContentValues();
            values.put("producto", producto);
            values.put("cantidad", 1);

            db.insert("carrito", null, values);

            Log.d(TAG, "agregarProducto: Producto insertado con cantidad 1.");
        }

        cursor.close();
        db.close();
    }

    /**
     * Resta una unidad del producto. Si la cantidad llega a 0, se elimina del carrito.
     */
    public void restarProducto(String producto) {
        Log.d(TAG, "restarProducto: Producto recibido -> " + producto);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT cantidad FROM carrito WHERE producto=?",
                new String[]{producto}
        );

        if (cursor.moveToFirst()) {

            int cantidad = cursor.getInt(0);
            Log.d(TAG, "restarProducto: Cantidad actual = " + cantidad);

            if (cantidad > 1) {
                ContentValues values = new ContentValues();
                values.put("cantidad", cantidad - 1);

                db.update("carrito", values, "producto=?", new String[]{producto});
                Log.d(TAG, "restarProducto: Cantidad reducida a " + (cantidad - 1));

            } else {
                db.delete("carrito", "producto=?", new String[]{producto});
                Log.d(TAG, "restarProducto: Producto eliminado del carrito (cantidad llegó a 0)");
            }
        } else {
            Log.w(TAG, "restarProducto: Producto no encontrado en carrito.");
        }

        cursor.close();
        db.close();
    }

    /**
     * Elimina completamente un producto del carrito.
     */
    public void eliminarProducto(String producto) {
        Log.d(TAG, "eliminarProducto: Eliminando producto -> " + producto);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("carrito", "producto=?", new String[]{producto});
        db.close();

        Log.d(TAG, "eliminarProducto: Eliminación completada.");
    }

    /**
     * Devuelve un Cursor con todos los productos y sus cantidades dentro del carrito.
     * El cursor debe cerrarse fuera en la clase que lo use.
     */
    public Cursor obtenerCarrito() {
        Log.d(TAG, "obtenerCarrito: Consultando carrito");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT producto, cantidad FROM carrito", null);

        Log.d(TAG, "obtenerCarrito: Registros encontrados = " + cursor.getCount());

        return cursor;
    }

    /**
     * Limpia completamente el carrito después de pagar.
     */
    public void limpiarCarrito() {
        Log.d(TAG, "limpiarCarrito: Eliminando todos los productos del carrito");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("carrito", null, null);
        db.close();

        Log.d(TAG, "limpiarCarrito: Carrito vacío.");
    }

    /**
     * Obtiene la cantidad de un producto específico dentro del carrito.
     */
    public int obtenerCantidadProducto(String nombre) {
        Log.d(TAG, "obtenerCantidadProducto: Consultando cantidad para -> " + nombre);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT cantidad FROM carrito WHERE producto = ?",
                new String[]{nombre}
        );

        if (cursor.moveToFirst()) {
            int cantidad = cursor.getInt(0);
            cursor.close();

            Log.d(TAG, "obtenerCantidadProducto: Cantidad encontrada = " + cantidad);
            return cantidad;
        }

        cursor.close();
        Log.w(TAG, "obtenerCantidadProducto: Producto no encontrado, regresando 0.");
        return 0;
    }

    /**
     * Crea un pedido en la tabla "pedidos" y devuelve su ID.
     */
    public int crearPedido(int total) {
        Log.d(TAG, "crearPedido: Creando pedido con total = " + total);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.d(TAG, "crearPedido: Fecha del pedido = " + fecha);

        ContentValues values = new ContentValues();
        values.put("fecha", fecha);
        values.put("total", total);

        long id = db.insert("pedidos", null, values);

        Log.d(TAG, "crearPedido: Pedido registrado con ID = " + id);

        return (int) id;
    }

    /**
     * Inserta un detalle de un pedido en la tabla detalle_pedido.
     */
    public void insertarDetalle(int idPedido, String producto, int cantidad) {
        Log.d(TAG, "insertarDetalle: Insertando detalle -> Pedido ID: "
                + idPedido + ", Producto: " + producto + ", Cantidad: " + cantidad);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_pedido", idPedido);
        values.put("id_producto", producto);
        values.put("cantidad", cantidad);

        db.insert("detalle_pedido", null, values);

        Log.d(TAG, "insertarDetalle: Detalle insertado correctamente.");
    }
}
