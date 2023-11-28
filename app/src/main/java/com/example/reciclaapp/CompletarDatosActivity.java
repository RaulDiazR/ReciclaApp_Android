package com.example.reciclaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class CompletarDatosActivity extends AppCompatActivity {

    EditText nombreField;
    EditText apellidosField;
    EditText telefonoField;

    TextView nombreTextView;
    TextView apellidosTextView;
    TextView telefonoTextView;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    Button continuarButton;

    CheckBox tycCheckBox;

    FirebaseAuth auth;
    FirebaseUser user;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_datos);
        initDatePicker();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        nombreField = findViewById(R.id.nombres);
        apellidosField = findViewById(R.id.apellidos);
        telefonoField = findViewById(R.id.phone);

        nombreTextView = findViewById(R.id.textView4);
        apellidosTextView = findViewById(R.id.textView2);
        telefonoTextView = findViewById(R.id.textView6);

        dateButton = findViewById(R.id.birthday);
        dateButton.setText(getTodaysDate());

        tycCheckBox = findViewById(R.id.checkBox1);

        continuarButton = findViewById(R.id.buttonContinuar);
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar selectedDate = new GregorianCalendar(year, month, day);
                Calendar currentDate = Calendar.getInstance();

                // Verificar si la fecha seleccionada es posterior a la fecha actual
                if (selectedDate.after(currentDate)) {
                    // Mostrar un mensaje de error o realizar alguna acción, ya que la fecha es inválida
                    Toast.makeText(CompletarDatosActivity.this, "Selecciona una fecha válida", Toast.LENGTH_SHORT).show();
                } else {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    dateButton.setText(date);
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = R.style.CustomDatePickerDialog;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }


    private String makeDateString(int day, int month, int year){
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month){
        if(month == 1)
            return "ENE";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "ABR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AGO";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DIC";
        //default should never happen
        return "ENE";
    }

    public void openDatePicker(View view){
        // Crear una vista para el fondo semitransparente
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
        datePickerDialog.show();
        datePickerDialog.setOnDismissListener(dialog -> rootView.removeView(backgroundView));
        //rootView.removeView(backgroundView);
    }

    public void goToTyC(View v){
        Intent intent = new Intent(this, TerminosCondicionesActivity.class);
        startActivity(intent);
    }

    public void goToAvPriv(View v){
        Intent intent = new Intent(this, AvisoprivacidadActivity.class);
        startActivity(intent);
    }

    public void ContinuarRegistro(View view){
        if (isFormValid()){
            String firstName, lastName, email, phoneNumber, dateOfBirth, password;
            int rank_points, highest1;
            FirebaseUser user = auth.getCurrentUser();
            firstName = nombreField.getText().toString();
            lastName = apellidosField.getText().toString();
            email = user.getEmail();
            phoneNumber = telefonoField.getText().toString();
            dateOfBirth = dateButton.getText().toString();
            rank_points = 0;
            highest1 = 0;
            updateUI(user);

            // Get a reference to the Firestore collection "users" and set the document with the user's UID
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("usuarios").document(user.getUid());

            userRef.update(
                    "nombres", nombreField.getText().toString(),
                    "apellidos", apellidosField.getText().toString(),
                    "telefono", telefonoField.getText().toString(),
                    "fechaNacimiento", dateButton.getText().toString()
            );
            updateUI(user);
            startActivity(new Intent(CompletarDatosActivity.this, VerNoticiasActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private boolean isFormValid() {
        boolean isValid = true;
        TextView firstErrorView = null;

        if (isEmpty(nombreField)) {
            nombreField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = nombreTextView;
            }
            isValid = false;
        }

        if (isEmpty(apellidosField)) {
            apellidosField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = apellidosTextView;
            }
            isValid = false;
        }

        if (isEmpty(telefonoField)) {
            telefonoField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = telefonoTextView;
            };
            isValid = false;
        }

        if (!isValidPhoneNumber(telefonoField.getText().toString())) {
            telefonoField.setError("El número debe tener 10 dígitos");
            if (firstErrorView == null) {
                firstErrorView = telefonoTextView;
            }
            isValid = false;
        }

        if (!(tycCheckBox.isChecked())) {
            tycCheckBox.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = tycCheckBox;
            }
            isValid = false;
        }

        if (!isValid) {
            if (firstErrorView != null) {
                ScrollView scrollView = findViewById(R.id.scrollView);
                scrollView.smoothScrollTo(0, firstErrorView.getTop());
            }
        }
        return isValid;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Patrón que verifica que el número de teléfono tenga exactamente 10 dígitos
        String regex = "^[0-9]{10}$";
        return Pattern.matches(regex, phoneNumber);
    }

    private boolean isEmpty (EditText editText){
        return editText.getText().toString().trim().isEmpty();
    }

    private void updateUI(FirebaseUser user) {

    }
}