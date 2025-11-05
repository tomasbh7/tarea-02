package com.example.tarea_02;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class CarritoActivity extends AppCompatActivity {

    private LinearLayout contenedorCarrito;
    private TextView txtSubtotalValue;
    private Map<String, Integer> productCounts = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contenedorCarrito = findViewById(R.id.layoutCarrito);
        txtSubtotalValue = findViewById(R.id.txt_subtotal_value);
        ImageView btnBack = findViewById(R.id.btn_back);
        Button btnPagar = findViewById(R.id.btn_pagar);
        btnBack.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productCounts")) {
            productCounts = (HashMap<String, Integer>) intent.getSerializableExtra("productCounts");
        }

        mostrarProductos();

        // Botón de pagar
        btnPagar.setOnClickListener(v -> {
            if (contenedorCarrito.getChildCount() > 0) {
                txtSubtotalValue.setText(R.string.gracias);
                contenedorCarrito.removeAllViews();
            }
        });
    }

    private void mostrarProductos() {
        contenedorCarrito.removeAllViews();
        int subtotal = 0;

        for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
            String nombre = entry.getKey();
            int cantidad = entry.getValue();

            if (cantidad > 0) {
                View card = getLayoutInflater().inflate(R.layout.item_producto, contenedorCarrito, false);

                TextView tvNombre = card.findViewById(R.id.tvNombreProducto);
                TextView tvPrecio = card.findViewById(R.id.tvPrecioProducto);
                TextView tvCantidad = card.findViewById(R.id.tvCantidadProducto);
                ImageView imgProducto = card.findViewById(R.id.imgProducto);
                ImageView btnEliminar = card.findViewById(R.id.btnEliminarProducto);

                int precioUnitario = asignarDatosProducto(nombre, tvNombre, tvPrecio, imgProducto);
                tvCantidad.setText(String.valueOf(cantidad));

                subtotal += precioUnitario * cantidad;

                btnEliminar.setOnClickListener(v -> {
                    productCounts.put(nombre, 0);
                    contenedorCarrito.removeView(card);
                    calcularSubtotal();
                });

                contenedorCarrito.addView(card);
            }
        }

        if (subtotal == 0) {
            TextView vacio = new TextView(this);
            vacio.setText(R.string.carritovacio);
            vacio.setTextSize(16);
            vacio.setPadding(20, 20, 20, 20);
            contenedorCarrito.addView(vacio);
        }

        txtSubtotalValue.setText(txtSubtotalValue.getContext().getString(R.string.formatoprecio, subtotal));
    }

    private int asignarDatosProducto(String nombre, TextView tvNombre, TextView tvPrecio, ImageView imgProducto) {
        int precio = 0;


        switch (nombre) {
            case "tacos":
                precio = obtenerPrecio("tacos");
                tvNombre.setText(R.string.tacos);
                imgProducto.setImageResource(R.drawable.taco);
                break;
            case "pizza":
                precio = obtenerPrecio("pizza");
                tvNombre.setText(R.string.pizza);
                imgProducto.setImageResource(R.drawable.pizza);
                break;
            case "hamburguesa":
                precio = obtenerPrecio("hamburguesa");
                tvNombre.setText(R.string.hamburguesa);
                imgProducto.setImageResource(R.drawable.hamburguesa);
                break;
            case "ensalada":
                precio = obtenerPrecio("ensalada");
                tvNombre.setText(R.string.ensalada);
                imgProducto.setImageResource(R.drawable.ensalada);
                break;
            case "papas":
                precio = obtenerPrecio("papas");
                tvNombre.setText(R.string.papasnombre);
                imgProducto.setImageResource(R.drawable.papas);
                break;
            case "frijoles":
                precio = obtenerPrecio("frijoles");
                tvNombre.setText(R.string.frijolesnombre);
                imgProducto.setImageResource(R.drawable.frijoles);
                break;
            case "bolitas":
                precio = obtenerPrecio("bolitas");
                tvNombre.setText(R.string.bolitas);
                imgProducto.setImageResource(R.drawable.bolitas);
                break;
            case "bebida":
                precio = obtenerPrecio("bebida");
                tvNombre.setText(R.string.bebida);
                imgProducto.setImageResource(R.drawable.bloxiade);
                break;
        }
        String precioString = tvPrecio.getContext().getString(R.string.formatoprecio, precio);
        tvPrecio.setText(precioString);
        return precio;
    }

    private void calcularSubtotal() {
        int subtotal = 0;
        for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
            String nombre = entry.getKey();
            int cantidad = entry.getValue();
            subtotal += obtenerPrecio(nombre) * cantidad;
        }

        if (subtotal > 0) {
            txtSubtotalValue.setText(txtSubtotalValue.getContext().getString(R.string.formatoprecio, subtotal));
        } else {
            txtSubtotalValue.setText(R.string.precio_producto);
            contenedorCarrito.removeAllViews();

            TextView vacio = new TextView(this);
            vacio.setText(R.string.carritovacio);
            vacio.setTextSize(16);
            vacio.setPadding(20, 20, 20, 20);
            contenedorCarrito.addView(vacio);
        }
    }

    private int obtenerPrecio(String nombre) {
        switch (nombre) {
            case "tacos": return 8;
            case "pizza": return 25;
            case "hamburguesa": return 9;
            case "ensalada": return 5;
            case "papas": return 4;
            case "frijoles": return 5;
            case "bolitas": return 7;
            case "bebida": return 5;
            default: return 0;
        }
    }
}
