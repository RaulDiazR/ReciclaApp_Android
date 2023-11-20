package com.example.reciclaapp;
/**
 * Clase que representa un elemento de la lista de selección de materiales.
 */
public class MaterialesSelectionItem {
    private final int imageResource; // Resource ID for the item's image
    private final String name; // Name or text associated with the item

    public MaterialesSelectionItem(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
    }
    /**
     * Obtiene el identificador de recurso de la imagen.
     */
    public int getImageResource() {
        return imageResource;
    }
    /**
     * Obtiene el nombre o texto asociado al elemento.
     */
    public String getText() {
        return name;
    }
}
