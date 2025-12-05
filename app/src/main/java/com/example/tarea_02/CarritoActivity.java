package com.example.tarea_02;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tarea_02.db.CarritoDAO;
public class CarritoActivity extends AppCompatActivity {

    private LinearLayout contenedorCarrito;
    private TextView txtSubtotalValue;
    private CarritoDAO carritoDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        configurarUI();
        carritoDAO = new CarritoDAO(this);
        mostrarProductos();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarProductos();
    }
    /**
     * Configura referencias de UI y listeners básicos
     */
    private void configurarUI() {
        contenedorCarrito = findViewById(R.id.layoutCarrito);
        txtSubtotalValue = findViewById(R.id.txt_subtotal_value);

        // Ajuste de pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.btn_back);
        Button btnPagar = findViewById(R.id.btn_pagar);

        btnBack.setOnClickListener(v -> finish());

        btnPagar.setOnClickListener(v -> pagarCarrito());
    }

    /**
     * Limpia y carga visualmente cada producto en el carrito
     */
    private void mostrarProductos() {

        contenedorCarrito.removeAllViews();
        Cursor cursor = carritoDAO.obtenerCarrito();

        int subtotal = 0;

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(0);
            int cantidad = cursor.getInt(1);

            View card = getLayoutInflater().inflate(R.layout.item_producto, contenedorCarrito, false);

            TextView tvNombre = card.findViewById(R.id.tvNombreProducto);
            TextView tvPrecio = card.findViewById(R.id.tvPrecioProducto);
            TextView tvCantidad = card.findViewById(R.id.tvCantidadProducto);
            ImageView imgProducto = card.findViewById(R.id.imgProducto);
            ImageView btnEliminar = card.findViewById(R.id.btnEliminarProducto);

            // asignar datos a la tarjeta
            int precio = asignarDatosProducto(nombre, tvNombre, tvPrecio, imgProducto);
            tvCantidad.setText(String.valueOf(cantidad));

            subtotal += precio * cantidad;

            btnEliminar.setOnClickListener(v -> {
                carritoDAO.eliminarProducto(nombre);
                mostrarProductos();
            });

            contenedorCarrito.addView(card);
        }

        cursor.close();

        if (subtotal == 0) {
            mostrarCarritoVacio();
        }

        txtSubtotalValue.setText(getString(R.string.formatoprecio, subtotal));
    }



    /**
     * Configura vistas según producto y devuelve su precio base
     */
    private int asignarDatosProducto(String nombre, TextView tvNombre, TextView tvPrecio, ImageView imgProducto) {
        int precio = obtenerPrecio(nombre);

        // Mapeo de recursos según el nombre
        switch (nombre) {
            case "tacos":
                tvNombre.setText(R.string.tacos);
                imgProducto.setImageResource(R.drawable.taco);
                break;
            case "pizza":
                tvNombre.setText(R.string.pizza);
                imgProducto.setImageResource(R.drawable.pizza);
                break;
            case "hamburguesa":
                tvNombre.setText(R.string.hamburguesa);
                imgProducto.setImageResource(R.drawable.hamburguesa);
                break;
            case "ensalada":
                tvNombre.setText(R.string.ensalada);
                imgProducto.setImageResource(R.drawable.ensalada);
                break;
            case "papas":
                tvNombre.setText(R.string.papasnombre);
                imgProducto.setImageResource(R.drawable.papas);
                break;
            case "frijoles":
                tvNombre.setText(R.string.frijolesnombre);
                imgProducto.setImageResource(R.drawable.frijoles);
                break;
            case "bolitas":
                tvNombre.setText(R.string.bolitas);
                imgProducto.setImageResource(R.drawable.bolitas);
                break;
            case "bebida":
                tvNombre.setText(R.string.bebida);
                imgProducto.setImageResource(R.drawable.bloxiade);
                break;
        }

        tvPrecio.setText(getString(R.string.formatoprecio, precio));
        return precio;
    }

    /**
     * Cuando el carrito queda vacío
     */
    private void mostrarCarritoVacio() {
        contenedorCarrito.removeAllViews();
        TextView vacio = new TextView(this);
        vacio.setText(R.string.carritovacio);
        vacio.setTextSize(16);
        vacio.setPadding(20, 20, 20, 20);
        contenedorCarrito.addView(vacio);
    }

    /**
     * Simula pago
     */
    private void pagarCarrito() {
        if (contenedorCarrito.getChildCount() > 0) {
            carritoDAO.limpiarCarrito();
            mostrarProductos();
            txtSubtotalValue.setText(R.string.gracias);
        }
    }

    /**
     * Devuelve precios base
     */
    private int obtenerPrecio(String nombre) {
        switch (nombre) {
            case "tacos": return 8;
            case "pizza": return 25;
            case "hamburguesa" : return 9;
            case "ensalada":
            case "frijoles":
            case "bebida":
                return 5;
            case "papas": return 4;
            case "bolitas": return 7;
            default: return 0;
        }
    }
}
