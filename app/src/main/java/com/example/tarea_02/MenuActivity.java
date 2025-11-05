package com.example.tarea_02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private Map<String, Integer> productCounts = new HashMap<>();
    private Map<String, TextView> countTextViews = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        Log.i("DEBUG", " MenuActivity cargado");
        initializeAllCounters();
        ImageButton btnCarrito = findViewById(R.id.btnCarrito);
        btnCarrito.setOnClickListener(v -> {
            Intent i = new Intent(MenuActivity.this, CarritoActivity.class);
            startActivity(i);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeAllCounters() {
        productCounts.putIfAbsent("tacos", 0);
        setupProductCounter("hamburguesa", R.id.btnIncrementHamburguesa,R.id.btnDecrementHamburguesa,R.id.tvCountHamburguesa);
        setupProductCounter("tacos", R.id.btnIncrementTacos, R.id.btnDecrementTacos, R.id.tvCountTacos);
        setupProductCounter("ensalada", R.id.btnIncrementEnsalada, R.id.btnDecrementEnsalada, R.id.tvCountEnsalada);
        setupProductCounter("pizza", R.id.btnIncrementPizza, R.id.btnDecrementPizza, R.id.tvCountPizza);
        setupProductCounter("papas", R.id.btnIncrementPapas, R.id.btnDecrementPapas, R.id.tvCountPapas);
        setupProductCounter("frijoles", R.id.btnIncrementFrijoles, R.id.btnDecrementFrijoles, R.id.tvCountFrijoles);
        setupProductCounter("bolitas", R.id.btnIncrementBolitas, R.id.btnDecrementBolitas, R.id.tvCountBolitas);
        setupProductCounter("bebida", R.id.btnIncrementBebida, R.id.btnDecrementBebida, R.id.tvCountBebida);
    }

    private void setupProductCounter(String productName, int incrementId, int decrementId, int countId) {
        productCounts.put(productName, 0);

        TextView btnIncrement = findViewById(incrementId);
        TextView btnDecrement = findViewById(decrementId);
        TextView tvCount = findViewById(countId);

        countTextViews.put(productName, tvCount);
        btnIncrement.setOnClickListener(v -> updateCounter(productName, 1));
        btnDecrement.setOnClickListener(v -> updateCounter(productName, -1));
        tvCount.setText("0");
    }

    private void updateCounter(String productName, int change) {
        int currentCount = productCounts.get(productName);
        int newCount = currentCount + change;
        if (newCount >= 0) {
            productCounts.put(productName, newCount);
            countTextViews.get(productName).setText(String.valueOf(newCount));
        }
    }
    }