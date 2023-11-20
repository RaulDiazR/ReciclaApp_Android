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
/**
 * DireccionActivity es una actividad de Android que permite a los usuarios ingresar y editar información de dirección.
 * Esta actividad recupera información del usuario, incluyendo nombre, apellidos, teléfono y dirección, desde Firestore
 * y permite a los usuarios actualizar su dirección y luego proceder a localizarla en un mapa.
 */
public class DireccionActivity extends AppCompatActivity {

    // Declarar referencias a campos de formulario y mensaje de error TextView
    EditText calleField;
    EditText numeroField;
    EditText coloniaField;
    EditText municipioField;
    EditText codigoPostalField;
    EditText telefonoField;

    // Obtenga referencias a elementos TextView para los campos obligatorios
    TextView calleTextView;
    TextView numeroTextView;
    TextView coloniaTextView;
    TextView municipioTextView;
    TextView codigoPostalTextView;
    TextView telefonoTextView;
    TextView errorTextView;

    String nombreCompleto;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);
        // Busque la barra de herramientas por su ID y configúrela como barra de aplicaciones
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Eliminar el título predeterminado de la barra de aplicaciones
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Habilitar el botón Atrás (navegación hacia arriba)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        // Crea una vista para el fondo semitransparente.
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);

        View dialogView = getLayoutInflater().inflate(R.layout.popup_progress, null);

        // Configurar el diálogo
        builder.setView(dialogView);

        // Personaliza el diálogo
        final AlertDialog alertDialog = builder.create();

        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        
        alertDialog.show();

        // Agregue la vista de fondo y muestre el diálogo.
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);

        // Inicialice las referencias a los campos del formulario y el mensaje de error.
        calleField = findViewById(R.id.Calle);
        numeroField = findViewById(R.id.numeroCasa);
        coloniaField = findViewById(R.id.colonia);
        municipioField = findViewById(R.id.municipio);
        codigoPostalField = findViewById(R.id.codigoPostal);
        telefonoField = findViewById(R.id.telefono);

        // Obtenga referencias a elementos TextView para los campos obligatorios
        calleTextView = findViewById(R.id.textView4);
        numeroTextView = findViewById(R.id.textView3);
        coloniaTextView = findViewById(R.id.textView2);
        municipioTextView = findViewById(R.id.textView1);
        codigoPostalTextView = findViewById(R.id.textView5);
        telefonoTextView = findViewById(R.id.textView6);

        // Obtenga una referencia al documento de Firestore
        userId = "user_id_2";
        // Se accede a la base de datos de FireStore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firestore.collection("usuarios").document(userId);

        // Obtener los datos de Firestore
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if (documentSnapshot.exists()) {
                    // EL documento existe
                    Map<String, Object> userData = documentSnapshot.getData();

                    if (userData != null) {
                        String nombre = (String) userData.get("nombre");
                        String apellidos = (String) userData.get("apellidos");
                        nombreCompleto = nombre + " " + apellidos;

                        String telefono = (String) userData.get("telefono");
                        telefonoField.setText(telefono);

                        Map<String, Object> direccionData = (Map<String, Object>) userData.get("direccion");

                        if (direccionData != null) {
                            // Set the data to the TextView elements
                            calleField.setText((String) direccionData.get("calle"));
                            numeroField.setText((String) direccionData.get("numero"));
                            coloniaField.setText((String) direccionData.get("colonia"));
                            municipioField.setText((String) direccionData.get("municipio"));
                            codigoPostalField.setText((String) direccionData.get("codigoPostal"));
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

    // Método llamado cuando se presiona el botón para localizar en el mapa
    public void localizar_en_mapa(View view) {
        String calleText = calleField.getText().toString();
        String numeroText = numeroField.getText().toString();
        String coloniaText = coloniaField.getText().toString();
        String municipioText = municipioField.getText().toString();
        String codigoPostalText = codigoPostalField.getText().toString();
        String telefonoText = telefonoField.getText().toString();

        // Se checa si el formulario es válido
        if (isFormValid()) {
            Intent intent = new Intent(this, SelfLocationStreetMapActivity.class);
            // se crea una dirección completa para agregar al apartado de userInfo de la recolección
            String direccion = calleText + " " + numeroText + ", " + "Colonia " + coloniaText+ ", " + municipioText + ", Código Postal " + codigoPostalText;
            Log.d("direccion", direccion);
            // se mandan por intent a la siguiente actividad toda la info relacionada a la dirección del usuario
            intent.putExtra("street", calleText);
            intent.putExtra("numero", numeroText);
            intent.putExtra("colonia", coloniaText);
            intent.putExtra("municipio", municipioText);
            intent.putExtra("postalcode", codigoPostalText);

            // esta info se adjunta dentro del apartado de userInfo de la recolección
            intent.putExtra("direccionCompleta", direccion);
            intent.putExtra("telefono", telefonoText);
            intent.putExtra("nombreCompleto", nombreCompleto);

            // Se pasa la información de la actividad previa
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
    }

    // Método llamado cuando se presiona el botón de navegación hacia arriba
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Verificar si el formulario es válido
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

        // Si el formulario es inválido, se hace un scroll hasta el primer error
        if (!isValid) {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.smoothScrollTo(0, errorTextView.getTop());
        }

        return isValid;
    }

    // Verificar si un EditText está vacío
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
    
}
