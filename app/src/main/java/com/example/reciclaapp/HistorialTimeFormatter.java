package com.example.reciclaapp;

import android.widget.NumberPicker;

import java.util.Locale;

public class HistorialTimeFormatter implements NumberPicker.Formatter {
    @Override
    public String format(int value) {
        // Format the value with a leading zero if it's less than 10
        return String.format(Locale.getDefault(),"%02d", value);
    }
}
