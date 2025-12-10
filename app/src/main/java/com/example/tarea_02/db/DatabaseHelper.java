package com.example.tarea_02.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Nombre y versión de la BD
    public static final String DB_NAME = "restaurante.db";
    public static final int DB_VERSION = 1;

    // Nombres de tablas
    public static final String TABLE_CARRITO = "carrito";
    public static final String TABLE_PEDIDOS = "pedidos";
    public static final String TABLE_DETALLE_PEDIDO = "detalle_pedido";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "Constructor: Base de datos inicializada -> " + DB_NAME);
    }

    /**
     * Se ejecuta únicamente cuando la BD se crea por primera vez.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Creando tablas de la base de datos...");

        // Tabla de carrito
        Log.d(TAG, "onCreate: Creando tabla -> " + TABLE_CARRITO);
        db.execSQL(
                "CREATE TABLE " + TABLE_CARRITO + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "producto TEXT," +
                        "cantidad INTEGER" +
                        ")"
        );

        // Tabla de pedidos
        Log.d(TAG, "onCreate: Creando tabla -> " + TABLE_PEDIDOS);
        db.execSQL(
                "CREATE TABLE " + TABLE_PEDIDOS + " (" +
                        "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "fecha TEXT," +
                        "total INTEGER" +
                        ")"
        );

        // Tabla de detalle de pedido
        Log.d(TAG, "onCreate: Creando tabla -> " + TABLE_DETALLE_PEDIDO);
        db.execSQL(
                "CREATE TABLE " + TABLE_DETALLE_PEDIDO + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_pedido INTEGER," +
                        "producto TEXT," +
                        "cantidad INTEGER," +
                        "FOREIGN KEY(id_pedido) REFERENCES " + TABLE_PEDIDOS + "(id_pedido)" +
                        ")"
        );

        Log.d(TAG, "onCreate: Todas las tablas fueron creadas correctamente.");
    }

    /**
     * Se ejecuta cuando se incrementa DB_VERSION.
     * Aquí se deben aplicar migraciones.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(TAG, "onUpgrade: Actualizando BD de versión " + oldVersion + " -> " + newVersion);

        Log.d(TAG, "onUpgrade: Eliminando tabla carrito...");
        db.execSQL("DROP TABLE IF EXISTS carrito");

        Log.d(TAG, "onUpgrade: Eliminando tabla pedidos...");
        db.execSQL("DROP TABLE IF EXISTS pedidos");

        Log.d(TAG, "onUpgrade: Eliminando tabla detalle_pedido...");
        db.execSQL("DROP TABLE IF EXISTS detalle_pedido");


        Log.d(TAG, "onUpgrade: Recreando tablas...");
        onCreate(db);

        Log.d(TAG, "onUpgrade: Actualización completada.");
    }
}
