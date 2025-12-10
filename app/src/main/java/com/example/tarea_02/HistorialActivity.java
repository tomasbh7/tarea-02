package com.example.tarea_02;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea_02.db.DatabaseHelper;

public class HistorialActivity extends AppCompatActivity {

    // TAG para los logs
    private static final String TAG = "HistorialActivity";

    private LinearLayout layoutHistorial;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        Log.d(TAG, "onCreate: Iniciando actividad de historial");

        layoutHistorial = findViewById(R.id.layoutHistorial);
        dbHelper = new DatabaseHelper(this);

        // Listener botón volver
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Log.d(TAG, "Botón volver presionado, cerrando actividad.");
            finish();
        });

        cargarHistorial();
    }

    /**
     * Método encargado de consultar todos los pedidos almacenados en la base de datos
     * y mostrarlos dinámicamente dentro del layout principal.
     */
    private void cargarHistorial() {

        Log.d(TAG, "cargarHistorial: Iniciando carga de datos...");

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta SQL: trae pedidos ordenados del más reciente al más viejo
        Cursor cursorPedidos = db.rawQuery(
                "SELECT id_pedido, fecha, total FROM pedidos ORDER BY id_pedido DESC",
                null
        );

        Log.d(TAG, "cargarHistorial: Cantidad de registros encontrados = " + cursorPedidos.getCount());

        LayoutInflater inflater = LayoutInflater.from(this);

        // Iterar por cada registro devuelto
        while (cursorPedidos.moveToNext()) {

            int idPedido = cursorPedidos.getInt(0);
            String fecha = cursorPedidos.getString(1);
            int total = cursorPedidos.getInt(2);

            Log.d(TAG, "Pedido leído -> ID: " + idPedido + ", Fecha: " + fecha + ", Total: " + total);

            // Inflar la tarjeta del historial
            View card = inflater.inflate(R.layout.item_historial, null);

            // Setear los campos de la card
            TextView txtId = card.findViewById(R.id.txt_id_pedido);
            TextView txtFecha = card.findViewById(R.id.txt_fecha_pedido);
            TextView txtTotal = card.findViewById(R.id.txt_total_pedido);

            txtId.setText("Pedido #" + idPedido);
            txtFecha.setText("Fecha: " + fecha);
            txtTotal.setText("Total: $" + total);

            // Agregar card al layout principal
            layoutHistorial.addView(card);
        }

        // Cierre de cursor y base de datos
        try {
            cursorPedidos.close();
            db.close();
            Log.d(TAG, "cargarHistorial: Cursor y base de datos cerrados correctamente.");
        } catch (Exception e) {
            Log.e(TAG, "cargarHistorial: Error al cerrar recursos.", e);
        }
    }
}
