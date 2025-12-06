package com.example.tarea_02.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "restaurante.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_PRODUCTOS = "productos";
    public static final String TABLE_CARRITO = "carrito";

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tabla de productos
        db.execSQL(
                "CREATE TABLE " + TABLE_PRODUCTOS + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "nombre TEXT UNIQUE," + "precioEntero INTEGER," + "precioTexto TEXT," + "descripcion TEXT" + ")"
        );

        // Tabla de carrito
        db.execSQL(
                "CREATE TABLE " + TABLE_CARRITO + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "producto TEXT," + "cantidad INTEGER" + ")"
        );

        // Insertar productos base
        ProductSeeder.insertarBase(context, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS carrito");
        onCreate(db);
    }
}
