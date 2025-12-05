package com.example.tarea_02.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CarritoDAO {

    private final DatabaseHelper dbHelper;

    public CarritoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void agregarProducto(String producto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT cantidad FROM carrito WHERE producto = ?", new String[]{producto});

        if (cursor.moveToFirst()) {
            int cantidadActual = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("cantidad", cantidadActual + 1);
            db.update("carrito", values, "producto=?", new String[]{producto});
        } else {
            ContentValues values = new ContentValues();
            values.put("producto", producto);
            values.put("cantidad", 1);
            db.insert("carrito", null, values);
        }

        cursor.close();
        db.close();
    }

    public void restarProducto(String producto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT cantidad FROM carrito WHERE producto=?",
                new String[]{producto});

        if (cursor.moveToFirst()) {
            int cantidad = cursor.getInt(0);

            if (cantidad > 1) {
                ContentValues values = new ContentValues();
                values.put("cantidad", cantidad - 1);
                db.update("carrito", values, "producto=?", new String[]{producto});
            } else {
                db.delete("carrito", "producto=?", new String[]{producto});
            }
        }

        cursor.close();
        db.close();
    }

    public void eliminarProducto(String producto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("carrito", "producto=?", new String[]{producto});
    }


    public Cursor obtenerCarrito() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery("SELECT producto, cantidad FROM carrito", null);
    }

    public void limpiarCarrito() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("carrito", null, null);
        db.close();
    }
}
