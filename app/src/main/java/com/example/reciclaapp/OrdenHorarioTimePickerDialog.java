package com.example.reciclaapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

/**
 * Diálogo personalizado que muestra un NumberPicker para seleccionar la hora y los minutos.
 */
public class OrdenHorarioTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private final Button buttonToUpdate;
    int initialHour, initialMinute;
    int minH, minM;

    public OrdenHorarioTimePickerDialog(Button buttonToUpdate, int iniHour, int iniMinute, int minH, int minM, int maxH, int maxM) {
        this.buttonToUpdate = buttonToUpdate;
        this.initialHour = iniHour;
        this.initialMinute = iniMinute;
        this.minH = minH;
        this.minM = minM;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.historial_time_picker, null);

        NumberPicker hourPicker = view.findViewById(R.id.hourPicker);
        NumberPicker minutePicker = view.findViewById(R.id.minutePicker);

        // Set the initial values based on the initialHour and initialMinute values
        hourPicker.setMinValue(minH);
        hourPicker.setMaxValue(18);
        hourPicker.setValue(initialHour);

        minutePicker.setMinValue(minM);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(initialMinute);

        // Set custom formatters to format the numbers with leading zeros
        hourPicker.setFormatter(new OrdenHorarioFormatter());
        minutePicker.setFormatter(new OrdenHorarioFormatter());

        // Create and return the custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view).setPositiveButton("Aceptar", (dialog, which) -> {
                    int selectedHour = hourPicker.getValue();
                    int selectedMinute = minutePicker.getValue();
                    this.updateTimeButtonText(buttonToUpdate, selectedHour, selectedMinute);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    // Handle cancel action if needed.
                });

        return builder.create();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Not used, as we handle time selection in the positive button click.
    }
    /**
     * Actualiza el texto del botón con la hora seleccionada.
     */
    private void updateTimeButtonText(Button buttonToUpdate, int hourOfDay, int minute) {
        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        buttonToUpdate.setText(selectedTime);
    }
}


