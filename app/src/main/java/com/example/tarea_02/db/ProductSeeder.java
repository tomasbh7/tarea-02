package com.example.tarea_02.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.tarea_02.R;

public class ProductSeeder {

    public static void insertarBase(Context context, SQLiteDatabase db) {

        insertar(db, context.getString(R.string.tacosnombre), 8, context.getString(R.string.tacosprecio), context.getString(R.string.tacosdescripcion));
        insertar(db, context.getString(R.string.hamburguesanombre), 9, context.getString(R.string.hamburguesaprecio), context.getString(R.string.hamburguesadescripcion));
        insertar(db, context.getString(R.string.ensaladanombre), 5, context.getString(R.string.ensaladaprecio), context.getString(R.string.ensaladadescripcion));
        insertar(db, context.getString(R.string.pizzanombre), 25, context.getString(R.string.pizzaprecio), context.getString(R.string.pizzadescripcion));
        insertar(db, context.getString(R.string.papasnombre), 4, context.getString(R.string.papasprecio), context.getString(R.string.papasdescripcion));
        insertar(db, context.getString(R.string.frijolesnombre), 5, context.getString(R.string.frijolesprecio), context.getString(R.string.frijolesdescripcion));
        insertar(db, context.getString(R.string.bolitasnombre), 7, context.getString(R.string.bolitasprecio), context.getString(R.string.bolitasdescripcion));
        insertar(db, context.getString(R.string.bebidanombre), 5, context.getString(R.string.bebidaprecio), context.getString(R.string.bebidadescripcion));
    }

    private static void insertar(SQLiteDatabase db, String nombre, int precioEntero, String precioTexto, String descripcion) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("precioEntero", precioEntero);
        values.put("precioTexto", precioTexto);
        values.put("descripcion", descripcion);

        db.insert("productos", null, values);
    }
}
