package com.example.tarea_02.model;

/**
 * Modelo que representa un producto del menú dentro de la aplicación.
 * Es utilizado por el RecyclerView para mostrar los elementos disponibles,
 * así como para obtener información cuando se agrega al carrito.
 *
 * Cada instancia contiene la información necesaria para mostrar un ítem:
 * imagen, nombre, descripción, precios y cantidad seleccionada.
 */
public class MenuItemModel {

    // Recurso drawable asociado al producto
    public int imageRes;

    // Nombre del producto (Ej: "Tacos", "Hamburguesa")
    public String nombre;

    // Descripción corta del producto
    public String descripcion;

    // Precio formateado como String para mostrar en UI (Ej: "$8.00")
    public String precio;

    // Precio en entero para cálculos con el carrito (Ej: 8)
    public Integer precioInt;

    // Cantidad seleccionada por el usuario en el carrito
    public int cantidad = 0;

    /**
     * Constructor del ítem de menú.
     *
     * @param imageRes   Recurso drawable del producto
     * @param nombre     Nombre del producto
     * @param descripcion Descripción del producto
     * @param precio     Precio en formato String como se muestra en UI
     * @param precioInt  Precio entero para cálculos internos
     */
    public MenuItemModel(int imageRes, String nombre, String descripcion, String precio, Integer precioInt) {
        this.imageRes = imageRes;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioInt = precioInt;
    }
}
