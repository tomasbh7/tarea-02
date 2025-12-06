package com.example.tarea_02;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarea_02.adapter.MenuAdapter;
import com.example.tarea_02.db.CarritoDAO;
import com.example.tarea_02.model.MenuHeaderModel;
import com.example.tarea_02.model.MenuItemModel;
import com.example.tarea_02.data.MenuData;


import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    RecyclerView recyclerMenu;
    MenuAdapter adapter;
    private TextView tvTotalCount;

    private CarritoDAO carritoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        tvTotalCount = findViewById(R.id.tvTotalCount);

        carritoDAO = new CarritoDAO(this);

        findViewById(R.id.btnCarrito).setOnClickListener(v ->
                startActivity(new Intent(MenuActivity.this, CarritoActivity.class)));

        setupRecycler();
        actualizarBadge();
    }


    private void setupRecycler() {

        recyclerMenu = findViewById(R.id.recyclerMenu);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this));

        List<Object> itemsUI = new ArrayList<>();
        List<MenuItemModel> productos = new ArrayList<>();

        // Crear productos
        MenuItemModel tacos = new MenuItemModel(R.drawable.taco, getString(R.string.tacosnombre), getString(R.string.tacosdescripcion), getString(R.string.tacosprecio), 8);
        MenuItemModel hamburguesa = new MenuItemModel(R.drawable.hamburguesa, getString(R.string.hamburguesanombre), getString(R.string.hamburguesadescripcion), getString(R.string.hamburguesaprecio), 9);
        MenuItemModel ensalada = new MenuItemModel(R.drawable.ensalada, getString(R.string.ensaladanombre), getString(R.string.ensaladadescripcion), getString(R.string.ensaladaprecio), 5);
        MenuItemModel pizza = new MenuItemModel(R.drawable.pizza, getString(R.string.pizzanombre), getString(R.string.pizzadescripcion), getString(R.string.pizzaprecio), 25);
        MenuItemModel papas = new MenuItemModel(R.drawable.papas, getString(R.string.papasnombre), getString(R.string.papasdescripcion), getString(R.string.papasprecio), 4);
        MenuItemModel frijoles = new MenuItemModel(R.drawable.frijoles, getString(R.string.frijolesnombre), getString(R.string.frijolesdescripcion), getString(R.string.frijolesprecio), 5);
        MenuItemModel bolitas = new MenuItemModel(R.drawable.bolitas, getString(R.string.bolitasnombre), getString(R.string.bolitasdescripcion), getString(R.string.bolitasprecio), 7);
        MenuItemModel bebida = new MenuItemModel(R.drawable.bloxiade, getString(R.string.bebidanombre), getString(R.string.bebidadescripcion), getString(R.string.bebidaprecio), 5);


        // CATEGORÍAS CON BATCH ADD
        addCategoria(itemsUI, productos, getString(R.string.comidas), tacos, hamburguesa, ensalada, pizza);

        addCategoria(itemsUI, productos, getString(R.string.extras), papas, frijoles, bolitas);

        addCategoria(itemsUI, productos, getString(R.string.bebidas), bebida);

        adapter = new MenuAdapter(this, itemsUI, productos, carritoDAO);
        adapter.setOnCartUpdatedListener(this::actualizarBadge);
        recyclerMenu.setAdapter(adapter);
        MenuData.setProductos(productos);
    }


    private void addCategoria(List<Object> itemsUI, List<MenuItemModel> productos, String titulo, MenuItemModel... models) {
        // Añade el header
        itemsUI.add(new MenuHeaderModel(titulo));

        // Añade todos los productos asociados
        for (MenuItemModel m : models) {
            itemsUI.add(m);
            productos.add(m);
        }
    }

    private void actualizarBadge() {
        Cursor c = carritoDAO.obtenerCarrito();
        int total = 0;

        while (c.moveToNext()) {
            total += c.getInt(1);
        }
        c.close();

        if (total > 0) {
            tvTotalCount.setVisibility(TextView.VISIBLE);
            tvTotalCount.setText(String.valueOf(total));
        } else {
            tvTotalCount.setVisibility(TextView.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.refreshQuantities();
        }
        actualizarBadge();
    }

}
