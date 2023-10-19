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
import java.util.Objects;

public class HistorialTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private final Button buttonToUpdate;
    int initialHour, initialMinute;
    int minH, minM;

    public HistorialTimePickerDialog(Button buttonToUpdate, int iniHour, int iniMinute, int minH, int minM) {
        this.buttonToUpdate = buttonToUpdate;
        this.initialHour = iniHour;
        this.initialMinute = iniMinute;
        this.minH = minH;
        this.minM = minM;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
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
        hourPicker.setFormatter(new HistorialTimeFormatter());
        minutePicker.setFormatter(new HistorialTimeFormatter());

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

    private void updateTimeButtonText(Button buttonToUpdate, int hourOfDay, int minute) {
        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        buttonToUpdate.setText(selectedTime);
    }
}


