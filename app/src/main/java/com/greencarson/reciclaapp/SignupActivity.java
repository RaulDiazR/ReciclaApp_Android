package com.greencarson.reciclaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    EditText nombresField;
    EditText apellidosField;
    EditText correoField;
    EditText telefonoField;
    EditText contrasenaField;
    EditText confirmarcontrasenaField;

    TextView nombresTextView;
    TextView apellidosTextView;
    TextView correoTextView;
    TextView telefonoTextView;
    TextView fechaTextView;
    TextView contrasenaTextView;
    TextView confirmarcontrasenaTextView;

    String contrasenaText;
    String confirmarcontrasenaText;

    CheckBox tycCheckBox;

    Button registroButton;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    TextView firstErrorView = null;

    // we will use this to match the sent otp from firebase
    private String storedVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initDatePicker();

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the app bar
        setSupportActionBar(toolbar);

        // Remove default title for app bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Enable the back button (up navigation)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombresField = findViewById(R.id.nombres);
        apellidosField = findViewById(R.id.apellidos);
        correoField = findViewById(R.id.mail);
        telefonoField = findViewById(R.id.phone);
        //fechaField = findViewById(R.id.birthday);
        contrasenaField = findViewById(R.id.password);
        confirmarcontrasenaField = findViewById(R.id.cpassword);

        nombresTextView = findViewById(R.id.textView4);
        apellidosTextView = findViewById(R.id.textView2);
        correoTextView = findViewById(R.id.textView3);
        telefonoTextView = findViewById(R.id.textView6);
        fechaTextView = findViewById(R.id.textView7);
        contrasenaTextView = findViewById(R.id.textView8);
        confirmarcontrasenaTextView = findViewById(R.id.textView9);

        tycCheckBox = findViewById(R.id.checkBox1);

        registroButton = findViewById(R.id.buttonRegistro);

        dateButton = findViewById(R.id.birthday);
        dateButton.setText(getTodaysDate());

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignupActivity.this, VerNoticiasActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
                    Toast.makeText(SignupActivity.this, "Selecciona una fecha válida", Toast.LENGTH_SHORT).show();
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

    public void goToLogin(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToTyC(View v){
        Intent intent = new Intent(this, TerminosCondicionesActivity.class);
        startActivity(intent);
    }

    public void goToAvPriv(View v){
        Intent intent = new Intent(this, AvisoprivacidadActivity.class);
        startActivity(intent);
    }

    public void Continuar(View v) {
        mAuth = FirebaseAuth.getInstance();
        if (isFormValid()) {
            String email, password;
            email = correoField.getText().toString();
            password = contrasenaField.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                // This code should be placed right after updateUI(user) in your existing code
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser usuario = mAuth.getCurrentUser();
                                    updateUI(user);

                                    // Crear una vista para el fondo semitransparente
                                    final View backgroundView = new View(SignupActivity.this);
                                    backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                                    backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);

                                    // Inflar el diseño personalizado
                                    View dialogView = getLayoutInflater().inflate(R.layout.confirmarregistro, null);

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

                                    // Configurar acciones de los botones
                                    Button btnConfirmar = dialogView.findViewById(R.id.ConfirmarButton);
                                    Button btnCancelar = dialogView.findViewById(R.id.CancelarButton);

                                    btnConfirmar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                            showActivarCuentaDialog(user, backgroundView);
                                        }
                                    });

                                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Lógica para Cancelar
                                            alertDialog.dismiss();
                                            deleteUserAndDismiss(user, backgroundView);
                                        }
                                    });

                                    alertDialog.setOnDismissListener(view -> {
                                        alertDialog.dismiss();
                                        deleteUserAndDismiss(user, backgroundView);
                                    });

                                    // Agregar la vista de fondo y mostrar el cuadro de diálogo
                                    FrameLayout rootView = findViewById(android.R.id.content);
                                    rootView.addView(backgroundView);
                                    alertDialog.show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                correoField.setError("Correo ya en uso");
                                if (firstErrorView == null) {
                                    firstErrorView = correoTextView;
                                }
                                ScrollView scrollView = findViewById(R.id.scrollView);
                                scrollView.smoothScrollTo(0, firstErrorView.getTop());
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private void showActivarCuentaDialog(FirebaseUser user, View backgroundView) {
        mAuth = FirebaseAuth.getInstance();
        String firstName, lastName, email, phoneNumber, dateOfBirth, fotoPerfil, password;
        int rank_points, highest1;
        firstName = nombresField.getText().toString();
        lastName = apellidosField.getText().toString();
        email = correoField.getText().toString();
        phoneNumber = telefonoField.getText().toString();
        dateOfBirth = dateButton.getText().toString();
        fotoPerfil = "";
        password = contrasenaField.getText().toString();
        rank_points = 0;
        highest1 = 0;
        user = mAuth.getCurrentUser();
        updateUI(user);

        // Crear una vista para el fondo semitransparente
        backgroundView = new View(SignupActivity.this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);

        // Inflar el diseño personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.activatucuenta, null);

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

        View finalBackgroundView = backgroundView;

        FirebaseUser finalUser = user;
        View finalBackgroundView1 = backgroundView;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            // Create a User object with additional data
                            mAuth = FirebaseAuth.getInstance();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            User newUser = new User(
                                    firstName, // Get the first name from your input field
                                    lastName,  // Get the last name from your input field
                                    email,
                                    phoneNumber, // Get the phone number from your input field
                                    dateOfBirth,  // Get the date of birth from your input field
                                    fotoPerfil,
                                    rank_points,
                                    highest1
                            );

                            // Get a reference to the Firestore collection "users" and set the document with the user's UID
                            DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("usuarios").document(user.getUid());

                            // Set the user data in Firestore
                            userDocRef.set(newUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "User data saved to Firestore");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Error saving user data to Firestore", e);
                                        }
                                    });
                            updateUI(user);
                            user.sendEmailVerification();
                            updateUI(user);
                            // Configurar acciones de los botones
                            Button btnContinuar = dialogView.findViewById(R.id.ContinuarVerif);
                            btnContinuar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mAuth = FirebaseAuth.getInstance();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    mAuth.signOut();
                                    Intent intent = new Intent(SignupActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            updateUI(null);
                            showActivarCuentaDialog(finalUser, finalBackgroundView1);
                        }
                    }
                });

        //user = mAuth.getCurrentUser();

        alertDialog.setOnDismissListener(view -> {
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(finalBackgroundView);
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Agregar la vista de fondo y mostrar el cuadro de diálogo
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(finalBackgroundView);
        alertDialog.show();
    }

    private void deleteUserAndDismiss(FirebaseUser user, View backgroundView) {
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User account deleted.");
                            }
                        }
                    });
        }

        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.removeView(backgroundView);
    }


    private boolean isFormValid() {
        boolean isValid = true;

        if (isEmpty(nombresField)) {
            nombresField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = nombresTextView;
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

        if (isEmpty(correoField)) {
            correoField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = correoTextView;
            }
            isValid = false;
        }

        if (!isValidEmail(correoField.getText().toString())){
            correoField.setError("Correo no válido");
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

        if (isEmpty(contrasenaField)) {
            contrasenaField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = contrasenaTextView;
            }
            isValid = false;
        }

        if (!isValidPassword(contrasenaField.getText().toString())) {
            contrasenaField.setError("Contraseña inválida. Debe tener al menos 8 caracteres, una mayúscula y un número");
            if (firstErrorView == null) {
                firstErrorView = contrasenaTextView;
            }
            isValid = false;
        }

        if (isEmpty(confirmarcontrasenaField)) {
            confirmarcontrasenaField.setError("Este campo es obligatorio");
            if (firstErrorView == null) {
                firstErrorView = contrasenaTextView;
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

        contrasenaText = contrasenaField.getText().toString();
        confirmarcontrasenaText = confirmarcontrasenaField.getText().toString();
        if(!contrasenaText.equals(confirmarcontrasenaText)){
            confirmarcontrasenaField.setError("Las contraseñas no coinciden");
            if (firstErrorView == null) {
                firstErrorView = confirmarcontrasenaTextView;
            }
            isValid = false;
        }

        // If your form is inside a ScrollView, scroll to the first error message.
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

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Método para verificar la validez de la contraseña
    private boolean isValidPassword(String password) {
        // Requisitos: Al menos 8 caracteres, una letra mayúscula y un número
        String regex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        return Pattern.matches(regex, password);
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void updateUI(FirebaseUser user) {

    }
}