package com.example.reciclaapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class DireccionActivity extends AppCompatActivity {

    // Declare references to form fields and error message TextView
    EditText calleField;
    EditText numeroField;
    EditText coloniaField;
    EditText municipioField;
    EditText codigoPostalField;
    EditText telefonoField;

    // Get references to TextView elements for required fields
    TextView calleTextView;
    TextView numeroTextView;
    TextView coloniaTextView;
    TextView municipioTextView;
    TextView codigoPostalTextView;
    TextView telefonoTextView;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);
        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Enable the back button (up navigation)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        // Create a view for the semitransparent background
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.popup_progress, null);

        // Configure the dialog
        builder.setView(dialogView);

        // Customize the dialog
        final AlertDialog alertDialog = builder.create();

        // Configure a semitransparent background
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        
        alertDialog.show();

        // Add the background view and show the dialog
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);

        // Se accede a la base de datos de FireStore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Initialize the references to form fields and error message
        calleField = findViewById(R.id.Calle);
        numeroField = findViewById(R.id.numeroCasa);
        coloniaField = findViewById(R.id.colonia);
        municipioField = findViewById(R.id.municipio);
        codigoPostalField = findViewById(R.id.codigoPostal);
        telefonoField = findViewById(R.id.telefono);
        EditText indicaciones = findViewById(R.id.indicaciones);

        // Get references to TextView elements for required fields
        calleTextView = findViewById(R.id.textView4);
        numeroTextView = findViewById(R.id.textView3);
        coloniaTextView = findViewById(R.id.textView2);
        municipioTextView = findViewById(R.id.textView1);
        codigoPostalTextView = findViewById(R.id.textView5);
        telefonoTextView = findViewById(R.id.textView6);

        // Get a reference to the Firestore document
        String userId = "user_id_2"; // Replace with the actual user ID
        DocumentReference userRef = firestore.collection("usuarios").document(userId);

        // Fetch the data from Firestore
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if (documentSnapshot.exists()) {
                    // The document exists in Firestore
                    Map<String, Object> userData = documentSnapshot.getData();

                    if (userData != null) {
                        Map<String, Object> direccionData = (Map<String, Object>) userData.get("direccion");

                        if (direccionData != null) {
                            // Set the data to the TextView elements
                            calleField.setText((String) direccionData.get("calle"));
                            numeroField.setText((String) direccionData.get("numero"));
                            coloniaField.setText((String) direccionData.get("colonia"));
                            municipioField.setText((String) direccionData.get("municipio"));
                            codigoPostalField.setText((String) direccionData.get("codigoPostal"));
                            telefonoField.setText((String) direccionData.get("telefono"));
                            indicaciones.setText((String) direccionData.get("indicaciones"));
                        }
                    }
                }
            }
            else {
                Log.d("FireStore", "Error when retrieving dirección from usuarios document: " + task.getException());
            }
            alertDialog.dismiss();
            rootView.removeView(backgroundView); // Remove the background
        });
    }

    public void localizar_en_mapa(View view) {
        String calleText = calleTextView.getText().toString();
        String numeroText = numeroTextView.getText().toString();
        String coloniaText = coloniaTextView.getText().toString();
        String municipioText = municipioTextView.getText().toString();
        String codigoPostalText = codigoPostalTextView.getText().toString();
        String telefonoText = telefonoTextView.getText().toString();
        TextView indicaciones = findViewById(R.id.indicaciones);
        String indicacionesText = indicaciones.getText().toString();

        // Check if the form is valid
        if (isFormValid()) {
            Intent intent = new Intent(this, SelfLocationStreetMapActivity.class);
            intent.putExtra("street", calleText);
            intent.putExtra("numero", numeroText);
            intent.putExtra("colonia", coloniaText);
            intent.putExtra("municipio", municipioText);
            intent.putExtra("postalcode", codigoPostalText);
            intent.putExtra("telefono", telefonoText);
            intent.putExtra("indicaciones", indicacionesText);

            // Se pasa la información de la actividad previa
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean isFormValid() {
        boolean isValid = true;

        if (isEmpty(telefonoField)) {
            telefonoField.setError("Este campo es obligatorio");
            errorTextView = telefonoTextView;
            isValid = false;
        }

        if (isEmpty(codigoPostalField)) {
            codigoPostalField.setError("Este campo es obligatorio");
            errorTextView = codigoPostalTextView;
            isValid = false;
        }

        if (isEmpty(municipioField)) {
            municipioField.setError("Este campo es obligatorio");
            errorTextView = municipioTextView;
            isValid = false;
        }

        if (isEmpty(coloniaField)) {
            coloniaField.setError("Este campo es obligatorio");
            errorTextView = coloniaTextView;
            isValid = false;
        }

        if (isEmpty(numeroField)) {
            numeroField.setError("Este campo es obligatorio");
            errorTextView = numeroTextView;
            isValid = false;
        }

        if (isEmpty(calleField)) {
            calleField.setError("Este campo es obligatorio");
            errorTextView = calleTextView;
            isValid = false;
        }

        // If your form is inside a ScrollView, scroll to the first error message.
        if (!isValid) {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.smoothScrollTo(0, errorTextView.getTop());
        }

        return isValid;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
    
}
