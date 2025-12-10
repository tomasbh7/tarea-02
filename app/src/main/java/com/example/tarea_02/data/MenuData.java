package com.example.tarea_02.data;

import java.util.ArrayList;
import java.util.List;
import com.example.tarea_02.model.MenuItemModel;

/**
 * Clase estática que funciona como un repositorio temporal de productos del menú.
 *
 * Es utilizada para que diversas partes de la aplicación (como el carrito o el historial)
 * puedan acceder a la lista completa de productos disponibles sin necesidad de volver
 * a instanciarlos o recrear estructuras.
 *
 * IMPORTANTE:
 * - Esta clase conserva los productos cargados en memoria mientras la app siga activa.
 * - No persiste la información en la base de datos.
 */
public class MenuData {

    /** Lista estática que contiene todos los productos disponibles en el menú. */
    private static final List<MenuItemModel> productos = new ArrayList<>();

    /**
     * Sobrescribe la lista actual de productos con una nueva.
     * Usado normalmente al iniciar el menú principal.
     *
     * @param lista Lista completa de productos cargada desde el menú.
     */
    public static void setProductos(List<MenuItemModel> lista) {
        productos.clear();
        productos.addAll(lista);
    }

    /**
     * Busca un producto dentro de la lista según su nombre.
     *
     * @param nombre Nombre del producto a buscar.
     * @return El objeto MenuItemModel si se encuentra, de lo contrario null.
     */
    public static MenuItemModel getProductoPorNombre(String nombre) {
        for (MenuItemModel m : productos) {
            if (m.nombre.equals(nombre)) return m;
        }
        return null;
    }
}
