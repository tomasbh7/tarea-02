package com.example.tarea_02.data;

import java.util.ArrayList;
import java.util.List;
import com.example.tarea_02.model.MenuItemModel;

public class MenuData {

    private static final List<MenuItemModel> productos = new ArrayList<>();

    public static void setProductos(List<MenuItemModel> lista) {
        productos.clear();
        productos.addAll(lista);
    }

    public static MenuItemModel getProductoPorNombre(String nombre) {
        for (MenuItemModel m : productos) {
            if (m.nombre.equals(nombre)) return m;
        }
        return null;
    }
}
