package com.example.reciclaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciclaapp.databinding.ActivityLoginBinding;
import com.example.reciclaapp.databinding.ActivityMainBinding;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    EditText correoField;
    EditText contrasenaField;

    TextView correoTextView;
    TextView contrasenaTextView;
    TextView errorTextView;

    String correoText;
    String contrasenaText;

    private static final String TAG = "EmailPassword";
    FirebaseAuth mAuth;

    Button btnGoogle;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth gAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoField = findViewById(R.id.mail);
        contrasenaField = findViewById(R.id.password);

        correoTextView = findViewById(R.id.textView4);
        contrasenaTextView = findViewById(R.id.textView5);

        mAuth = FirebaseAuth.getInstance();

        // Assign variable
        btnGoogle = findViewById(R.id.buttonGoogle);

        // Initialize sign in options the client-id is copied form google-services.json file
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("901697229886-hhlg1ttba01pkmicerijb5hn5chgkp91.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);

        btnGoogle.setOnClickListener((View.OnClickListener) view -> {
            // Initialize sign in intent
            Intent intent = googleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent, 100);
        });

        // Initialize firebase auth
        gAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = gAuth.getCurrentUser();
        // Check condition
        if (firebaseUser != null) {
            // When user already sign in redirect to profile activity
            startActivity(new Intent(LoginActivity.this, NewsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        gAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Check condition
                                if (task.isSuccessful()) {
                                    // When task is successful redirect to profile activity display Toast
                                    startActivity(new Intent(LoginActivity.this, NewsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    displayToast("Firebase authentication successful");
                                } else {
                                    // When task is unsuccessful display Toast
                                    displayToast("Authentication Failed :" + task.getException().getMessage());
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void goToRegistro(View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void Continuar (View v){
        if (isFormValid()) {
            correoText = correoField.getText().toString();
            contrasenaText = contrasenaField.getText().toString();
            mAuth.signInWithEmailAndPassword(correoText, contrasenaText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Intent intent = new Intent(LoginActivity.this, NewsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(0,0);
                            } else {
                                final View backgroundView = new View(LoginActivity.this);
                                backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                                backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                                // Inflar el diseño personalizado
                                View dialogView = getLayoutInflater().inflate(R.layout.contrasenaincorrrecta, null);

                                // Configurar el diálogo
                                builder.setView(dialogView);

                                // Personalizar el diálogo
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                                // Configurar un fondo semitransparente
                                Window window = alertDialog.getWindow();
                                if (window != null) {
                                    WindowManager.LayoutParams params = window.getAttributes();
                                    params.gravity = Gravity.CENTER;
                                    window.setAttributes(params);
                                }

                                alertDialog.show();

                                // Configurar acciones de los botones
                                Button btnVolverIntentar = dialogView.findViewById(R.id.TryAgainButton);

                                btnVolverIntentar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Lógica para Cancelar
                                        alertDialog.dismiss();
                                        FrameLayout rootView = findViewById(android.R.id.content);
                                        rootView.removeView(backgroundView);
                                    }
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
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    public void ContinuarFacebook (View v){
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }

    public void ContinuarGoogle (View v){
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }

    private boolean isFormValid() {
        boolean isValid = true;

        if (isEmpty(correoField)) {
            correoField.setError("Este campo es obligatorio");
            errorTextView = correoTextView;
            isValid = false;
        }

        if (isEmpty(contrasenaField)) {
            contrasenaField.setError("Este campo es obligatorio");
            errorTextView = contrasenaTextView;
            isValid = false;
        }
        return isValid;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void updateUI(FirebaseUser user) {

    }
}