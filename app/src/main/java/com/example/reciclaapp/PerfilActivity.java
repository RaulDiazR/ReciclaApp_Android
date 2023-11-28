package com.example.reciclaapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.regex.Pattern;

public class PerfilActivity extends AppCompatActivity {

    EditText nombreField;
    EditText apellidosField;
    EditText correoField;
    EditText telefonoField;

    TextView nombreTextView;
    TextView apellidosTextView;
    TextView correoTextView;
    TextView telefonoTextView;
    TextView fechanacimientoTextView;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    FirebaseAuth auth;
    FirebaseUser user;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        initDatePicker();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the Users collection
        DocumentReference userRef = db.collection("usuarios").document(user.getUid());

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the app bar
        setSupportActionBar(toolbar);

        // Remove default title for app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Enable the back button (up navigation)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombreField = findViewById(R.id.nombres);
        apellidosField = findViewById(R.id.apellidos);
        correoField = findViewById(R.id.mail);
        telefonoField = findViewById(R.id.phone);
        dateButton = findViewById(R.id.birthday);

        nombreTextView = findViewById(R.id.textView4);
        apellidosTextView = findViewById(R.id.textView2);
        correoTextView = findViewById(R.id.textView3);
        telefonoTextView = findViewById(R.id.textView6);
        fechanacimientoTextView = findViewById(R.id.textView7);

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(PerfilActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        // Get user information
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Populate EditText fields with user information
                nombreField.setText(documentSnapshot.getString("nombres"));
                apellidosField.setText(documentSnapshot.getString("apellidos"));
                correoField.setText(documentSnapshot.getString("correo"));
                correoField.setEnabled(false);
                telefonoField.setText(documentSnapshot.getString("telefono"));
                dateButton.setText(documentSnapshot.getString("fechaNacimiento"));
            }
        }).addOnFailureListener(e -> {
            // Handle failure, if any
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            Calendar selectedDate = new GregorianCalendar(year, month, day);
            Calendar currentDate = Calendar.getInstance();

            // Verificar si la fecha seleccionada es posterior a la fecha actual
            if (selectedDate.after(currentDate)) {
                // Mostrar un mensaje de error o realizar alguna acción, ya que la fecha es inválida
                Toast.makeText(PerfilActivity.this, "Selecciona una fecha válida", Toast.LENGTH_SHORT).show();
            } else {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
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
    }

    public void GuardarCambios(View view) {
        if (isFormValid()){
            // Crear una vista para el fondo semitransparente
            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Inflar el diseño personalizado
            View dialogView = getLayoutInflater().inflate(R.layout.guardarcambios, null);

            // Configurar el diálogo
            builder.setView(dialogView);

            // Personalizar el diálogo
            final AlertDialog alertDialog = builder.create();

            // Configurar un fondo semitransparente
            Window window = alertDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.CENTER;
                window.setAttributes(params);
            }

            alertDialog.show();

            // Configurar acciones de los botones
            Button btnConfirmar = dialogView.findViewById(R.id.ConfirmarButton);
            Button btnCancelar = dialogView.findViewById(R.id.CancelarButton);

            btnConfirmar.setOnClickListener(v -> {
                // Update Firestore with new user information
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userRef = db.collection("usuarios").document(user.getUid());

                userRef.update(
                        "nombres", nombreField.getText().toString(),
                        "apellidos", apellidosField.getText().toString(),
                        "correo", correoField.getText().toString(),
                        "telefono", telefonoField.getText().toString(),
                        "fechaNacimiento", dateButton.getText().toString()
                ).addOnSuccessListener(aVoid -> {
                    // Handle success
                    Toast.makeText(PerfilActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(PerfilActivity.this, "Error al guardar cambios", Toast.LENGTH_SHORT).show();
                });
                // Lógica para Confirmar
                Intent intent = new Intent(PerfilActivity.this, SettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });

            btnCancelar.setOnClickListener(v -> {
                // Lógica para Cancelar
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView);
            });

            alertDialog.setOnDismissListener(v -> {
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView);
            });

            // Agregar la vista de fondo y mostrar el cuadro de diálogo
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.addView(backgroundView);
            alertDialog.show();
        }
    }

    public void logOut(View v){
        // Crear una vista para el fondo semitransparente
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflar el diseño personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.cerrarsesion, null);

        // Configurar el diálogo
        builder.setView(dialogView);

        // Personalizar el diálogo
        final AlertDialog alertDialog = builder.create();

        // Configurar un fondo semitransparente
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

        alertDialog.show();

        // Configurar acciones de los botones
        Button btnConfirmar = dialogView.findViewById(R.id.ConfirmarButton);
        Button btnCancelar = dialogView.findViewById(R.id.CancelarButton);

        btnConfirmar.setOnClickListener(v12 -> {
            // Sign out from google
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                // Check condition
                if (task.isSuccessful()) {
                    // When task is successful sign out from firebase
                    auth.signOut();
                    // Finish activity
                    Intent intent = new Intent(PerfilActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });

            // Lógica para Confirmar
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnCancelar.setOnClickListener(v1 -> {
            // Lógica para Cancelar
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(backgroundView);
        });

        alertDialog.setOnDismissListener(view -> {
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(backgroundView);
        });

        // Agregar la vista de fondo y mostrar el cuadro de diálogo
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
        alertDialog.show();
    }

    private boolean isFormValid() {
        boolean isValid = true;
        TextView firstErrorView = null;

        if (isEmpty(nombreField)) {
            nombreField.setError("Este campo es obligatorio");
            firstErrorView = nombreTextView;
            isValid = false;
        }

        if (isEmpty(apellidosField)) {
            apellidosField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = apellidosTextView;
            }
            isValid = false;
        }

        if (isEmpty(correoField)) {
            correoField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = correoTextView;
            }
            isValid = false;
        }

        if (isEmpty(telefonoField)) {
            telefonoField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = telefonoTextView;
            }
            isValid = false;
        }

        if (!isValidPhoneNumber(telefonoField.getText().toString())) {
            telefonoField.setError("El número debe tener 10 dígitos");
            if (firstErrorView == null) {
                firstErrorView = telefonoTextView;
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
}