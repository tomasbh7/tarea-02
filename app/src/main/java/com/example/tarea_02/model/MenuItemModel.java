package com.example.tarea_02.model;

public class MenuItemModel {
    public int imageRes;
    public String nombre;
    public String descripcion;
    public String precio;
    public Integer precioInt;
    public int cantidad = 0;

    public MenuItemModel(int imageRes, String nombre, String descripcion, String precio, Integer precioInt) {
        this.imageRes = imageRes;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioInt = precioInt;
    }
}
