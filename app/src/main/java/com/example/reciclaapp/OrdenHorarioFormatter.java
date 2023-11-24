package com.example.reciclaapp;

import android.widget.NumberPicker;

import java.util.Locale;
/**
 * Clase que implementa la interfaz NumberPicker.Formatter para formatear los valores del NumberPicker.
 */
public class OrdenHorarioFormatter implements NumberPicker.Formatter {
    @Override
    public String format(int value) {
        // Formatea el valor con un cero inicial si es menor que 10
        return String.format(Locale.getDefault(),"%02d", value);
    }
}
