package com.greencarson.reciclaapp;

import android.view.View;
/*
   Interfaz HistorialItemClickListener: Define un método de clic para gestionar eventos de clic
   en elementos del RecyclerView en la pantalla de historial.

*/
public interface HistorialItemClickListener {
    void onClick(View v, int pos);
}
