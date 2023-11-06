package com.example.reciclaapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class OrdenHorarioActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button timeIni;
    private Button timeEnd;
    int hourIni, minuteIni;
    int hourEnd, minuteEnd;
    // Check if the user's time is after the finishing time of 7pm
    boolean isDayOver;
    // Check if the user's time is before the starting time of 7am
    boolean isBeforeDayStart;
    // guarda la hora y minuto que seleccionó el usuario para el inicio de la recolección
    int[] finalTimeIni = new int[2];
    // guarda la hora y minuto que seleccionó el usuario para el final de la recolección
    int[] finalTimeEnd  = new int[2];
    // guarda la fecha que seleccionó el usuario para el final de la recolección
    int[] finalDate  = new int[3];
    String curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden_horario);
        this.finalDate[0] = 0;
        this.finalDate[1] = 0;
        this.finalDate[2] = 0;

        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Enable the back button (up navigation)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        int curHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        isDayOver = curHour >= 19;
        isBeforeDayStart = curHour < 7;

        initDatePicker();
        dateButton = findViewById(R.id.fecha);
        dateButton.setText(getTodaysDate());

        // Button for "Hora de Inicio" - set the initial time
        timeIni = findViewById(R.id.tiempo_inicio);
        timeIni.setText(getTodaysTimeIni());

        // Button for "Hora Final" - set the time based on the initial time
        timeEnd = findViewById(R.id.tiempo_final);
        timeEnd.setText(getTodaysTimeEnd());
    }

    private String getTodaysTimeIni() {
        int hourIni = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteIni = Calendar.getInstance().get(Calendar.MINUTE);

        if (this.isDayOver || this.isBeforeDayStart) {
            hourIni = 7;
            minuteIni = 0;
        }
        this.hourIni = hourIni;
        this.minuteIni = minuteIni;
        return String.format(Locale.getDefault(), "%02d:%02d", hourIni, minuteIni);
    }

    private String getTodaysTimeEnd() {
        int hourEnd = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteEnd = Calendar.getInstance().get(Calendar.MINUTE);

        if (this.isDayOver || this.isBeforeDayStart) {
            hourEnd = 8;
            minuteEnd = 0;
        } else {
            if (hourEnd <= 17) {
                hourEnd = hourEnd + 1;
            } else {
                hourEnd = 18;
                minuteEnd = 59;
            }
        }

        this.hourEnd = hourEnd;
        this.minuteEnd = minuteEnd;
        return String.format(Locale.getDefault(), "%02d:%02d", hourEnd, minuteEnd);
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (isDayOver) {
            day = day + 1;
        }
        this.finalDate[0] = day;
        this.finalDate[1] = month;
        this.finalDate[2] = year;
        this.curDate = formatDate(day, month, year);
        return curDate;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            this.finalDate[0] = day;
            this.finalDate[1] = month;
            this.finalDate[2] = year;
            String formattedDate = formatDate(day, month, year);
            // Date changes
            dateButton.setText(formattedDate);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = R.style.CustomDatePickerDialog;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        if (isDayOver) {
            // Get the current date
            Calendar calendar = Calendar.getInstance();

            // Calculate the timestamp for the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            long nextDayTimestamp = calendar.getTimeInMillis();

            // Set the minimum date for the DatePickerDialog
            datePickerDialog.getDatePicker().setMinDate(nextDayTimestamp);
        } else {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
    }

    private String formatDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); // Subtract 1 from the month since Calendar uses 0-based indexing.
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d 'de' MMM 'de' yyyy", new Locale("es", "MX"));
        return dateFormat.format(cal.getTime());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    // Create a separate method to handle the "Hora de Inicio" button click
    public void openTimePickerIni(View view) {
        int minH = hourIni;
        int minM = minuteIni;
        String startTimeText = timeIni.getText().toString();
        String[] startTimeParts = startTimeText.split(":");
        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1]);
        if(notSameDay() || this.isBeforeDayStart) {
            minH = 7;
            minM = 0;
        }
        showCustomTimePickerDialog(timeIni, startHour, startMinute, minH, minM);
    }

    // Create a separate method to handle the "Hora Final" button click
    public void openTimePickerEnd(View view) {
        int minH = hourIni+1;
        int minM = 0;
        if (minH >= 19) {
            minH = hourIni;
            minM = 59;
        }
        String endTimeText = timeEnd.getText().toString();
        String[] endTimeParts = endTimeText.split(":");
        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1]);
        if(notSameDay() || this.isBeforeDayStart) {
            minH = 8;
            minM = 0;
        }
        showCustomTimePickerDialog(timeEnd, endHour, endMinute, minH, minM);
    }

    // checks if the user's selected date is different from the current phone's date
    private boolean notSameDay() {
        String curDate = this.curDate;
        String dateSelected = dateButton.getText().toString();
        return !dateSelected.equals(curDate);
    }

    // Define the method to show the custom time picker dialog
    private void showCustomTimePickerDialog(Button buttonToUpdate, int hour, int minute, int minH, int minM) {
        OrdenHorarioTimePickerDialog customDialog = new OrdenHorarioTimePickerDialog(buttonToUpdate, hour, minute, minH, minM);
        customDialog.show(getSupportFragmentManager(), "historial_time_picker");
    }

    // Checks if the user's selected time range is valid
    private boolean isTimeDifferenceValid() {
        // Parse the selected time from timeEnd button
        String endTimeText = timeEnd.getText().toString();
        String[] endTimeParts = endTimeText.split(":");
        this.finalTimeEnd[0] = Integer.parseInt(endTimeParts[0]);
        this.finalTimeEnd[1] = Integer.parseInt(endTimeParts[1]);

        // Parse the selected time from timeIni button
        String startTimeText = timeIni.getText().toString();
        String[] startTimeParts = startTimeText.split(":");
        this.finalTimeIni[0] = Integer.parseInt(startTimeParts[0]);
        this.finalTimeIni[1] = Integer.parseInt(startTimeParts[1]);

        // Calculate the time difference in minutes
        int endTimeInMinutes = finalTimeEnd[0] * 60 + finalTimeEnd[1];
        int startTimeInMinutes = finalTimeIni[0] * 60 + finalTimeIni[1];
        int timeDifference = endTimeInMinutes - startTimeInMinutes;

        // Check if the time difference is positive
        if (timeDifference < 0) {
            Toast.makeText(this, "El tiempo seleccionado es Inválido", Toast.LENGTH_LONG).show();
            return false;
        }
        // Check if the time difference is at least 30 minutes
        else if (timeDifference < 30) {
            Toast.makeText(this, "La recolección debe tener al menos 30 minutos de tiempo disponible", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    public boolean checkComentarios(EditText comentarios) {
        int currentLength = comentarios.getText().length();

        if (currentLength > 300) {
            comentarios.setError("Máximo de 300 caracteres");
            return false; // Return false to indicate that the input is invalid
        } else {
            comentarios.setError(null); // Clear any previous error message
            return true; // Return true to indicate that the input is valid
        }
    }


    public void finishTimeSelection (View v) {
        // Retrieve text from the EditText
        EditText editTextComentarios = findViewById(R.id.editTextComentarios);
        // Access the RadioGroup
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        // Get the ID of the selected RadioButton
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (isTimeDifferenceValid() && checkComentarios(editTextComentarios)) {
            String comentariosText = editTextComentarios.getText().toString(); //Comentarios
            boolean enPersona = selectedRadioButtonId == R.id.entrega_persona; // modo de entregar los desechos
            Intent intent = new Intent(this, DireccionActivity.class);
            intent.putExtra("fecha", this.finalDate);
            intent.putExtra("tiempoIni", this.finalTimeIni);
            intent.putExtra("tiempoEnd", this.finalTimeEnd);
            intent.putExtra("comentarios", comentariosText);
            intent.putExtra("enPersona", enPersona);
            startActivity(intent);

            // this.finalTimeIni; // tiempo de inicio de recolección
            // this.finalTimeEnd; // tiempo de finalización de recolección
            /*Toast toast = Toast.makeText(this, ""+
                    comentariosText+" -- "+
                    enPersona+" -- "+
                    this.finalDate[0]+", "+this.finalDate[1]+", "+this.finalDate[2]+" -- "+
                    this.finalTimeIni[0]+":"+
                    this.finalTimeIni[1]+" -- "+
                    this.finalTimeEnd[0]+":"+
                    this.finalTimeEnd[1], Toast.LENGTH_LONG);
            toast.show();*/
        }
    }

}
