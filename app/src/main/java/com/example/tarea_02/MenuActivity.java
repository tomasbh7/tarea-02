package com.example.tarea_02;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tarea_02.adapter.MenuAdapter;
import com.example.tarea_02.db.CarritoDAO;
import com.example.tarea_02.model.MenuHeaderModel;
import com.example.tarea_02.model.MenuItemModel;
import com.example.tarea_02.data.MenuData;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MenuActivity";

    private MenuAdapter adapter;
    private TextView tvTotalCount;

    private CarritoDAO carritoDAO;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Log.d(TAG, "onCreate: Iniciando MenuActivity");

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tvTotalCount = findViewById(R.id.tvTotalCount);

        configurarToolbar();
        configurarDrawer();

        carritoDAO = new CarritoDAO(this);

        findViewById(R.id.btnCarrito).setOnClickListener(v -> {
            Log.d(TAG, "Botón Carrito presionado: Abriendo CarritoActivity");
            startActivity(new Intent(MenuActivity.this, CarritoActivity.class));
        });

        setupRecycler();
        actualizarBadge();
    }

    /**
     * Configura el Toolbar como ActionBar de la actividad.
     */
    private void configurarToolbar() {
        Log.d(TAG, "configurarToolbar: Configurando toolbar");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Implementa el drawer lateral y su sincronización con el toolbar.
     */
    private void configurarDrawer() {
        Log.d(TAG, "configurarDrawer: Configurando Navigation Drawer");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                findViewById(R.id.toolbar),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Manejo de selección de elementos del menú lateral.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Log.d(TAG, "onNavigationItemSelected: Selección -> " + item.getTitle());

        if (id == R.id.nav_menu) {
            Toast.makeText(this, "Menú Principal", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_carrito) {
            startActivity(new Intent(this, CarritoActivity.class));
        } else if (id == R.id.nav_historial) {
            startActivity(new Intent(this, HistorialActivity.class));
        } else if (id == R.id.nav_promociones) {
            Toast.makeText(this, "Promociones", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_config) {
            Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_acerca) {
            Toast.makeText(this, "Acerca de la aplicación", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Control del botón atrás para cerrar el drawer si está abierto.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Log.d(TAG, "onBackPressed: Drawer abierto, cerrando");
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, "onBackPressed: Drawer cerrado, volviendo atrás normalmente.");
            super.onBackPressed();
        }
    }

    /**
     * Inflar el menú superior (3 puntos).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: Inflando menú");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Manejo de selección de opciones del menú superior (tres puntos).
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: Selección -> " + item.getTitle());

        if (id == android.R.id.home) {
            Toast.makeText(this, "Menú hamburguesa presionado", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.menu_historial) {
            startActivity(new Intent(this, HistorialActivity.class));
            return true;
        } else if (id == R.id.menu_promociones) {
            Toast.makeText(this, "Promociones", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_acerca) {
            Toast.makeText(this, "Acerca de la aplicación", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Configura el RecyclerView con categorías y productos.
     */
    private void setupRecycler() {

        Log.d(TAG, "setupRecycler: Preparando lista de menú");

        RecyclerView recyclerMenu = findViewById(R.id.recyclerMenu);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this));

        List<Object> itemsUI = new ArrayList<>();
        List<MenuItemModel> productos = new ArrayList<>();

        // Crear y registrar productos
        MenuItemModel tacos = new MenuItemModel(R.drawable.taco, getString(R.string.tacosnombre), getString(R.string.tacosdescripcion), getString(R.string.tacosprecio), 8);
        MenuItemModel hamburguesa = new MenuItemModel(R.drawable.hamburguesa, getString(R.string.hamburguesanombre), getString(R.string.hamburguesadescripcion), getString(R.string.hamburguesaprecio), 9);
        MenuItemModel ensalada = new MenuItemModel(R.drawable.ensalada, getString(R.string.ensaladanombre), getString(R.string.ensaladadescripcion), getString(R.string.ensaladaprecio), 5);
        MenuItemModel pizza = new MenuItemModel(R.drawable.pizza, getString(R.string.pizzanombre), getString(R.string.pizzadescripcion), getString(R.string.pizzaprecio), 25);
        MenuItemModel papas = new MenuItemModel(R.drawable.papas, getString(R.string.papasnombre), getString(R.string.papasdescripcion), getString(R.string.papasprecio), 4);
        MenuItemModel frijoles = new MenuItemModel(R.drawable.frijoles, getString(R.string.frijolesnombre), getString(R.string.frijolesdescripcion), getString(R.string.frijolesprecio), 5);
        MenuItemModel bolitas = new MenuItemModel(R.drawable.bolitas, getString(R.string.bolitasnombre), getString(R.string.bolitasdescripcion), getString(R.string.bolitasprecio), 7);
        MenuItemModel bebida = new MenuItemModel(R.drawable.bloxiade, getString(R.string.bebidanombre), getString(R.string.bebidadescripcion), getString(R.string.bebidaprecio), 5);

        Log.d(TAG, "setupRecycler: Generando categorías");

        addCategoria(itemsUI, productos, getString(R.string.comidas), tacos, hamburguesa, ensalada, pizza);
        addCategoria(itemsUI, productos, getString(R.string.extras), papas, frijoles, bolitas);
        addCategoria(itemsUI, productos, getString(R.string.bebidas), bebida);

        adapter = new MenuAdapter(this, itemsUI, productos, carritoDAO);
        adapter.setOnCartUpdatedListener(() -> {
            Log.d(TAG, "setupRecycler: Carrito actualizado, actualizando badge");
            actualizarBadge();
        });

        recyclerMenu.setAdapter(adapter);

        MenuData.setProductos(productos);

        Log.d(TAG, "setupRecycler: Menú configurado correctamente.");
    }

    /**
     * Añade una categoría y sus productos a la lista del recycler.
     */
    private void addCategoria(List<Object> itemsUI, List<MenuItemModel> productos, String titulo, MenuItemModel... models) {

        Log.d(TAG, "addCategoria: Agregando categoría -> " + titulo);

        itemsUI.add(new MenuHeaderModel(titulo));

        for (MenuItemModel m : models) {
            Log.d(TAG, "addCategoria: Producto agregado -> " + m.nombre);
            itemsUI.add(m);
            productos.add(m);
        }
    }

    /**
     * Actualiza el número de ítems en el carrito (badge).
     */
    private void actualizarBadge() {
        Log.d(TAG, "actualizarBadge: Consultando carrito");

        Cursor c = carritoDAO.obtenerCarrito();
        int total = 0;

        while (c.moveToNext()) {
            total += c.getInt(1);
        }
        c.close();

        Log.d(TAG, "actualizarBadge: Cantidad total = " + total);

        if (total > 0) {
            tvTotalCount.setVisibility(TextView.VISIBLE);
            tvTotalCount.setText(String.valueOf(total));
        } else {
            tvTotalCount.setVisibility(TextView.GONE);
        }
    }

    /**
     * Refresca cantidades de productos al volver a la actividad.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Actualizando cantidades y badge");

        if (adapter != null) {
            adapter.refreshQuantities();
        }
        actualizarBadge();
    }
}
