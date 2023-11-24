package com.example.reciclaapp;

import android.view.View;
/**
 * Interfaz para gestionar los clics en la vista de selección de materiales.
 */
public interface MaterialesSelectionClickListener {
    // Método llamado cuando se hace clic en un elemento de la lista.
    void onClick(View v, int position);
}
