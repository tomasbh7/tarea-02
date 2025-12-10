package com.example.tarea_02;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tarea_02.db.CarritoDAO;
import com.example.tarea_02.model.MenuItemModel;
import com.example.tarea_02.data.MenuData;

public class CarritoActivity extends AppCompatActivity {

    private static final String TAG = "CarritoActivity";

    private LinearLayout contenedorCarrito;
    private TextView txtSubtotalValue;
    private CarritoDAO carritoDAO;

    // Subtotal almacenado para el proceso de pago
    private int subtotalActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        Log.d(TAG, "onCreate: Inicializando interfaz del carrito.");

        configurarUI();

        carritoDAO = new CarritoDAO(this);

        mostrarProductos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Refrescando productos del carrito.");
        mostrarProductos();
    }

    /**
     * Configura los elementos de UI, listeners y padding de la actividad.
     */
    private void configurarUI() {

        Log.d(TAG, "configurarUI: Configurando elementos visuales...");

        contenedorCarrito = findViewById(R.id.layoutCarrito);
        txtSubtotalValue = findViewById(R.id.txt_subtotal_value);

        // Ajusta la UI al sistema de barras (edge to edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Log.d(TAG, "Botón volver presionado. Cerrando actividad.");
            finish();
        });

        findViewById(R.id.btn_pagar).setOnClickListener(v -> {
            Log.d(TAG, "Botón pagar presionado. Procesando compra...");
            pagarCarrito();
        });
    }

    /**
     * Muestra todos los productos del carrito dinámicamente.
     */
    private void mostrarProductos() {

        Log.d(TAG, "mostrarProductos: Cargando productos desde la BD...");

        contenedorCarrito.removeAllViews();
        Cursor cursor = carritoDAO.obtenerCarrito();

        int subtotal = 0;

        if (cursor == null || cursor.getCount() == 0) {
            Log.d(TAG, "mostrarProductos: Carrito vacío.");
            mostrarCarritoVacio();
            subtotalActual = 0;
            return;
        }

        Log.d(TAG, "mostrarProductos: Productos encontrados = " + cursor.getCount());

        while (cursor.moveToNext()) {

            String nombre = cursor.getString(0);
            int cantidad = cursor.getInt(1);

            Log.d(TAG, "Producto -> Nombre: " + nombre + " | Cantidad: " + cantidad);

            View card = getLayoutInflater().inflate(R.layout.item_producto, contenedorCarrito, false);

            TextView tvNombre = card.findViewById(R.id.tvNombreProducto);
            TextView tvPrecio = card.findViewById(R.id.tvPrecioProducto);
            TextView tvCantidad = card.findViewById(R.id.tvCantidadProducto);
            ImageView imgProducto = card.findViewById(R.id.imgProducto);
            ImageView btnEliminar = card.findViewById(R.id.btnEliminarProducto);

            MenuItemModel modelo = MenuData.getProductoPorNombre(nombre);

            if (modelo != null) {
                tvNombre.setText(modelo.nombre);
                imgProducto.setImageResource(modelo.imageRes);
                tvPrecio.setText(modelo.precio);

                int subtotalItem = modelo.precioInt * cantidad;
                subtotal += subtotalItem;

                Log.d(TAG, "Subtotal parcial del ítem = " + subtotalItem);
            } else {
                Log.e(TAG, "ERROR: Modelo no encontrado para el producto: " + nombre);
            }

            tvCantidad.setText(String.valueOf(cantidad));

            // Listener para eliminar un ítem
            btnEliminar.setTag(nombre);
            btnEliminar.setOnClickListener(this::eliminarProducto);

            contenedorCarrito.addView(card);
        }

        cursor.close();

        subtotalActual = subtotal;
        txtSubtotalValue.setText(getString(R.string.formatoprecio, subtotal));

        Log.d(TAG, "mostrarProductos: Subtotal calculado = " + subtotalActual);
    }

    /**
     * Elimina un producto del carrito.
     */
    public void eliminarProducto(View v) {

        String nombre = v.getTag().toString();
        Log.d(TAG, "eliminarProducto: Solicitando eliminación de -> " + nombre);

        carritoDAO.eliminarProducto(nombre);
        mostrarProductos();
    }

    /**
     * Muestra un mensaje indicando que el carrito está vacío.
     */
    private void mostrarCarritoVacio() {
        Log.d(TAG, "mostrarCarritoVacio: Mostrando mensaje de carrito vacío.");

        contenedorCarrito.removeAllViews();
        TextView vacio = new TextView(this);
        vacio.setText(R.string.carritovacio);
        vacio.setTextSize(16);
        vacio.setPadding(20, 20, 20, 20);
        contenedorCarrito.addView(vacio);
    }

    /**
     * Procesa el pedido: crea un pedido, registra los detalles y limpia el carrito.
     */
    private void pagarCarrito() {

        Log.d(TAG, "pagarCarrito: Iniciando proceso de pago...");

        Cursor cursor = carritoDAO.obtenerCarrito();

        if (cursor == null || cursor.getCount() == 0) {
            Log.w(TAG, "pagarCarrito: Carrito vacío, no se puede pagar.");
            txtSubtotalValue.setText(R.string.carritovacio);
            return;
        }

        Log.d(TAG, "pagarCarrito: Subtotal final = " + subtotalActual);

        // Crear el pedido
        int idPedido = carritoDAO.crearPedido(subtotalActual);
        Log.d(TAG, "pagarCarrito: Pedido creado con ID = " + idPedido);

        while (cursor.moveToNext()) {

            String nombreProducto = cursor.getString(0);
            int cantidad = cursor.getInt(1);

            MenuItemModel modelo = MenuData.getProductoPorNombre(nombreProducto);

            if (modelo != null) {
                carritoDAO.insertarDetalle(idPedido, modelo.nombre, cantidad);
                Log.d(TAG, "Detalle insertado para Producto: " + modelo.nombre + " | Cantidad: " + cantidad);
            } else {
                Log.e(TAG, "ERROR: No se encontró modelo para insertar detalle de: " + nombreProducto);
            }
        }

        cursor.close();

        carritoDAO.limpiarCarrito();
        Log.d(TAG, "pagarCarrito: Carrito limpiado.");

        mostrarProductos();
        txtSubtotalValue.setText(getString(R.string.gracias));

        Log.d(TAG, "pagarCarrito: Proceso finalizado correctamente.");
    }
}
