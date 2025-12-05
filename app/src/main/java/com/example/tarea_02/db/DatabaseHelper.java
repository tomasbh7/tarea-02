package com.example.tarea_02.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "restaurante.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_PRODUCTOS = "productos";
    public static final String TABLE_CARRITO = "carrito";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tabla de productos
        db.execSQL(
                "CREATE TABLE " + TABLE_PRODUCTOS + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "nombre TEXT UNIQUE," + "precio INTEGER" + ")"
        );

        // Tabla de carrito
        db.execSQL(
                "CREATE TABLE " + TABLE_CARRITO + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "producto TEXT," + "cantidad INTEGER" + ")"
        );

        // Insertar productos base
        db.execSQL("INSERT INTO productos (nombre, precio) VALUES ('hamburguesa', 9)");
        db.execSQL("INSERT INTO productos (nombre, precio) VALUES ('tacos', 8)");
        db.execSQL("INSERT INTO productos (nombre, precio) VALUES ('ensalada', 5)");
        db.execSQL("INSERT INTO productos (nombre, precio) VALUES ('pizza', 25)");
        db.execSQL("INSERT INTO productos (nombre, precio) VALUES ('papas', 4)");
        db.execSQL("INSERT INTO productos (nombre, precio) VALUES ('frijoles', 5)");
        db.execSQL("INSERT INTO productos (nombre, precio) VALUES ('bolitas', 7)");
        db.execSQL("INSERT INTO productos (nombre, precio) VALUES ('bebida', 5)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS carrito");
        onCreate(db);
    }
}
