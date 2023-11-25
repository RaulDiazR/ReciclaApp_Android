package com.example.reciclaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @SuppressLint("ClickableViewAccessibility")
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
            final View backgroundView = new View(LoginActivity.this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente
            // Initialize sign in intent
            Intent intent = googleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent, 100);
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.addView(backgroundView);
            // Agregar un OnTouchListener para detectar clics fuera del cuadro de diálogo
            backgroundView.setOnTouchListener((v, event) -> {
                rootView.removeView(backgroundView); // Eliminar la vista semitransparente
                return true;
            });
            // Initialize firebase auth
            gAuth = FirebaseAuth.getInstance();
            // Initialize firebase user
            FirebaseUser firebaseUser = gAuth.getCurrentUser();
            // Check condition
        });

        // Initialize firebase auth
        gAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = gAuth.getCurrentUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, VerNoticiasActivity.class);
            startActivity(intent);
            finish();
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
                        gAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (authResult.getAdditionalUserInfo().isNewUser()) {
                                    String firstName, lastName, email, phoneNumber, dateOfBirth, password;
                                    int rank_points, highest1;
                                    FirebaseUser user = gAuth.getCurrentUser();
                                    firstName = "";
                                    lastName = "";
                                    email = user.getEmail();
                                    phoneNumber = "";
                                    dateOfBirth = "";
                                    rank_points = 0;
                                    highest1 = 0;
                                    updateUI(user);
                                    // Create a User object with additional data
                                    User newUser = new User(
                                            firstName, // Get the first name from your input field
                                            lastName,  // Get the last name from your input field
                                            email,
                                            phoneNumber, // Get the phone number from your input field
                                            dateOfBirth,  // Get the date of birth from your input field
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
                                    // When task is successful redirect to profile activity display Toast
                                    startActivity(new Intent(LoginActivity.this, CompletarDatosActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    displayToast("Firebase authentication successful");
                                } else {
                                    // When task is unsuccessful display Toast
                                    startActivity(new Intent(LoginActivity.this, VerNoticiasActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    displayToast("Firebase authentication successful");
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

    public void Ingresar (View v){
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
                                checkUserInFirestore(user.getUid());
                            } else {
                                showErrorDialog();
                            }
                        }
                    });
        }
    }

    private void checkUserInFirestore(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("usuarios"); // Change to your collection name

        usersCollection.document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUserisVerificated(user.getUid());
                        } else {
                            // User does not exist in Firestore, show error message
                            showErrorDialog();
                            mAuth.signOut();
                            updateUI(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure, show error message
                        showErrorDialog();
                        updateUI(null);
                    }
                });
    }

    private void checkUserisVerificated(String uid) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user.isEmailVerified()){
            Intent intent = new Intent(LoginActivity.this, VerNoticiasActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Correo no verificado", Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorDialog(){
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

    public void ContinuarFacebook (View v){
        Intent intent = new Intent(this, VerNoticiasActivity.class);
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