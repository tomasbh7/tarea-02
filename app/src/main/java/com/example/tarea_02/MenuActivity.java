package com.example.tarea_02;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tarea_02.db.CarritoDAO;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private CarritoDAO carritoDAO;
    private TextView tvTotalCount;

    // Mapa que guarda referencias a los TextView del contador
    private final Map<String, TextView> countTextViews = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        carritoDAO = new CarritoDAO(this);

        configurarToolbar();
        inicializarUI();
        inicializarContadores();
        actualizarUIContadores();
        configurarInsetsPantalla();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarUIContadores();
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void inicializarUI() {
        tvTotalCount = findViewById(R.id.tvTotalCount);

        findViewById(R.id.btnCarrito).setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, CarritoActivity.class)));
    }

    /**
     * Enlaza cada producto con sus contadores y botones
     */
    private void inicializarContadores() {
        setupProductCounter("hamburguesa", R.id.btnIncrementHamburguesa, R.id.btnDecrementHamburguesa, R.id.tvCountHamburguesa);
        setupProductCounter("tacos",        R.id.btnIncrementTacos,        R.id.btnDecrementTacos,        R.id.tvCountTacos);
        setupProductCounter("ensalada",     R.id.btnIncrementEnsalada,     R.id.btnDecrementEnsalada,     R.id.tvCountEnsalada);
        setupProductCounter("pizza",        R.id.btnIncrementPizza,        R.id.btnDecrementPizza,        R.id.tvCountPizza);
        setupProductCounter("papas",        R.id.btnIncrementPapas,        R.id.btnDecrementPapas,        R.id.tvCountPapas);
        setupProductCounter("frijoles",     R.id.btnIncrementFrijoles,     R.id.btnDecrementFrijoles,     R.id.tvCountFrijoles);
        setupProductCounter("bolitas",      R.id.btnIncrementBolitas,      R.id.btnDecrementBolitas,      R.id.tvCountBolitas);
        setupProductCounter("bebida",       R.id.btnIncrementBebida,       R.id.btnDecrementBebida,       R.id.tvCountBebida);
    }

    private void setupProductCounter(String productName, int incrementId, int decrementId, int countId) {

        TextView tvCount = findViewById(countId);
        countTextViews.put(productName, tvCount);

        findViewById(incrementId).setOnClickListener(v -> modificarProducto(productName, 1));
        findViewById(decrementId).setOnClickListener(v -> modificarProducto(productName, -1));

        tvCount.setText("0");
    }

    /**
     * Incrementa o decrementa en BD y refresca UI
     */
    private void modificarProducto(String producto, int cambio) {
        if (cambio == 1) {
            carritoDAO.agregarProducto(producto);
        } else {
            carritoDAO.restarProducto(producto);
        }

        actualizarUIContadores();
    }

    /**
     * Lee datos desde SQLite y actualiza contadores visuales
     */
    private void actualizarUIContadores() {

        int total = 0;

        // Primero ponemos todo a 0 temporalmente
        for (String key : countTextViews.keySet()) {
            TextView tv = countTextViews.get(key);
            if (tv != null) tv.setText("0");
        }

        // Ahora consultamos SQLite
        Cursor cursor = carritoDAO.obtenerCarrito();

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(0);
            int cantidad = cursor.getInt(1);

            total += cantidad;

            TextView tv = countTextViews.get(nombre);
            if (tv != null) {
                tv.setText(String.valueOf(cantidad));
            }
        }

        cursor.close();

        // Finalmente actualizamos el badge del carrito
        if (total > 0) {
            tvTotalCount.setVisibility(View.VISIBLE);
            tvTotalCount.setText(String.valueOf(total));
        } else {
            tvTotalCount.setVisibility(View.GONE);
        }
    }


    private void configurarInsetsPantalla() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_historial) {
            mostrarMensaje("Historial de pedidos");
            return true;

        } else if (id == R.id.menu_promociones) {
            mostrarMensaje("Promociones");
            return true;

        } else if (id == R.id.menu_acerca) {
            mostrarMensaje("Acerca de la aplicación");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
