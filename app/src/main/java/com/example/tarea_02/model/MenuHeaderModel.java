package com.example.tarea_02.model;

/**
 * Modelo que representa un encabezado dentro del RecyclerView del menú.
 * Se utiliza para agrupar visualmente productos bajo una categoría
 * (por ejemplo: "Comidas", "Bebidas", "Extras", etc.).
 *
 * Esta clase funciona como un simple contenedor de texto
 * y es identificado en el adaptador para mostrar un item tipo header.
 */
public class MenuHeaderModel {

    // Nombre o título de la categoría del menú.
    public String titulo;

    /**
     * Constructor del header de categoría.
     *
     * @param titulo Nombre de la categoría que aparecerá en el RecyclerView
     */
    public MenuHeaderModel(String titulo) {
        this.titulo = titulo;
    }
}
