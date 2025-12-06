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

    private void configurarUI() {
        contenedorCarrito = findViewById(R.id.layoutCarrito);
        txtSubtotalValue = findViewById(R.id.txt_subtotal_value);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_pagar).setOnClickListener(v -> pagarCarrito());
    }

    private void mostrarProductos() {

        contenedorCarrito.removeAllViews();
        Cursor cursor = carritoDAO.obtenerCarrito();

        int subtotal = 0;

        if (cursor == null || cursor.getCount() == 0) {
            mostrarCarritoVacio();
            return;
        }

        while (cursor.moveToNext()) {

            String nombre = cursor.getString(0);
            int cantidad = cursor.getInt(1);

            View card = getLayoutInflater().inflate(R.layout.item_producto, contenedorCarrito, false);

            TextView tvNombre = card.findViewById(R.id.tvNombreProducto);
            TextView tvPrecio = card.findViewById(R.id.tvPrecioProducto);
            TextView tvCantidad = card.findViewById(R.id.tvCantidadProducto);
            ImageView imgProducto = card.findViewById(R.id.imgProducto);
            ImageView btnEliminar = card.findViewById(R.id.btnEliminarProducto);

            //Busca el modelo asociado
            MenuItemModel modelo = MenuData.getProductoPorNombre(nombre);

            if (modelo != null) {
                tvNombre.setText(modelo.nombre);
                imgProducto.setImageResource(modelo.imageRes);
                tvPrecio.setText(modelo.precio);
                subtotal += modelo.precioInt * cantidad;
            } else {
                tvNombre.setText(nombre);
                tvPrecio.setText(getString(R.string.inicial));
            }

            tvCantidad.setText(String.valueOf(cantidad));

            //Asignar nombre real para eliminar desde DB
            btnEliminar.setTag(nombre);
            btnEliminar.setOnClickListener(this::eliminarProducto);

            contenedorCarrito.addView(card);
        }

        cursor.close();

        txtSubtotalValue.setText(getString(R.string.formatoprecio, subtotal));
    }

    public void eliminarProducto(View v) {
        String nombre = v.getTag().toString();
        Log.d("Carrito","Eliminar producto = "+nombre);
        carritoDAO.eliminarProducto(nombre);
        mostrarProductos();
    }

    private void mostrarCarritoVacio() {
        contenedorCarrito.removeAllViews();
        TextView vacio = new TextView(this);
        vacio.setText(R.string.carritovacio);
        vacio.setTextSize(16);
        vacio.setPadding(20, 20, 20, 20);
        contenedorCarrito.addView(vacio);
    }

    private void pagarCarrito() {
        if (contenedorCarrito.getChildCount() > 0) {
            carritoDAO.limpiarCarrito();
            mostrarProductos();
            txtSubtotalValue.setText(R.string.gracias);
        }
    }
}
