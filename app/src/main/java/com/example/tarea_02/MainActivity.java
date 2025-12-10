package com.example.tarea_02;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // TAG para mensajes en Logcat
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Log inicial indicando que la app está arrancando
        Log.d(TAG, "onCreate: Iniciando aplicación");

        // Habilita el diseño Edge-to-Edge
        EdgeToEdge.enable(this);
        // Intent para redirigir inmediatamente a la actividad principal (MenuActivity)
        Log.d(TAG, "onCreate: Redirigiendo a MenuActivity");
        Intent i = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(i);

        // Finaliza esta actividad para evitar regresar a ella con el botón atrás
        Log.d(TAG, "onCreate: MainActivity finalizada para evitar retorno.");
        finish();
    }
}
