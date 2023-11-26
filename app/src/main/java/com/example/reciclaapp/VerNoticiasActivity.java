package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VerNoticiasActivity extends AppCompatActivity implements VerNoticiasItemClickListener {

    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;

    // 1- AdapterView
    RecyclerView recyclerView;
    // 2- DataSource
    List<VerNoticiasItem> newsList;
    // 3- Adapter
    VerNoticiasAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_noticias);

        auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.nombreUsuario);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            // Initialize Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Reference to the Users collection
            DocumentReference userRef = db.collection("usuarios").document(user.getUid());
            // Get user information

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Populate EditText fields with user information
                    String msg = "Hola " + documentSnapshot.getString("nombre");
                    textView.setText(msg);
                }
            }).addOnFailureListener(e -> {
                // Handle failure, if any
                Toast.makeText(VerNoticiasActivity.this, "Error loading user information", Toast.LENGTH_SHORT).show();
            });
        }

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // Set item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId != R.id.inicio) {
                // Navigate to the corresponding activity
                navigateToActivity(itemId);
            }
            return true;
        });

        // Initialize the selected item based on the current activity
        int currentItemId = getCurrentItemIdForActivity();
        bottomNavigationView.setSelectedItemId(currentItemId);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize newsList and newsAdapter
        newsList = new ArrayList<>(); // You might fetch this from Firebase or any other source
        //newsList.add(new VerNoticiasItem("titulo", "Askldkaodkopajsdkscjlkdopaskdoaksodkoapskdpaksdpkaspdkopasd", "Lopez Doriga", "MeroLelo"));
        newsAdapter = new VerNoticiasAdapter(newsList);
        recyclerView.setAdapter(newsAdapter);

        newsAdapter.setClickListener(this);

        fetchNewsData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchNewsData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference newsRef = db.collection("noticias");

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Order by 'fecha' attribute in descending order (newest first)
        newsRef.orderBy("fecha", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<VerNoticiasItem> dataList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Parse the data and create VerNoticiasItem objects
                        String title = documentSnapshot.getString("titulo");
                        String content = documentSnapshot.getString("cuerpo");
                        String author = documentSnapshot.getString("autor");
                        String fotoUrl = documentSnapshot.getString("imagen");

                        VerNoticiasItem newsItem = new VerNoticiasItem(title, content, author, fotoUrl);
                        dataList.add(newsItem);
                    }

                    // Pass the ordered data to the adapter
                    newsList.clear();
                    newsList.addAll(dataList);
                    progressBar.setVisibility(View.GONE);
                    newsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle failure, if any
                    Toast.makeText(VerNoticiasActivity.this, "Error loading news data", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToActivity(int itemId) {
        Intent intent = null;

        if (itemId == R.id.mapa) {
            intent = new Intent(this, StreetMapActivity.class);
        } else if (itemId == R.id.reciclaje) {
            intent = new Intent(this, HistorialRecoleccionesActivity.class);
        } else if (itemId == R.id.ajustes) {
            intent = new Intent(this, SettingsActivity.class);
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    private int getCurrentItemIdForActivity() {
        Class<?> currentClass = this.getClass();

        if (currentClass == StreetMapActivity.class) {
            return R.id.mapa;
        } else if (currentClass == HistorialRecoleccionesActivity.class) {
            return R.id.reciclaje;
        } else if (currentClass == SettingsActivity.class) {
            return R.id.ajustes;
        } else {
            return R.id.inicio;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Update the selected item in the bottom navigation view
        int currentItemId = getCurrentItemIdForActivity();
        bottomNavigationView.setSelectedItemId(currentItemId);
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View v, int position) {
        Toast.makeText(this, "Elemento: " + position + " Con titulo: " + newsList.get(position).getTitle(), Toast.LENGTH_LONG).show();

    }
}