package com.example.reciclaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class MaterialesActivity extends AppCompatActivity {
    // 1- AdapterView
    RecyclerView recyclerView;
    // 2- DataSource
    List<HistorialItem> itemList;
    // 3- Adapter
    MaterialesHeaderAdapter headerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiales);

        // Find the Toolbar by its ID and et the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewMateriales);

        // Create a MaterialesHeaderItem instance with the necessary data
        MaterialesHeaderItem headerData = new MaterialesHeaderItem("vie 14 de Aug de 2023", "12:00");

        // Create an instance of MaterialesHeaderAdapter and provide the headerData
        headerAdapter = new MaterialesHeaderAdapter(this, headerData);

        // Create a ConcatAdapter and add the headerAdapter first and then the content adapter
        //ConcatAdapter concatAdapter = new ConcatAdapter(headerAdapter, historialAdapter);

        // create and set the linear layout manager so the recycler view works properly like a scrolling view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(headerAdapter);

    }

    public void finishMaterialSelection (View v) {
        if (headerAdapter.getItemCount()-1 > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Debe seleccionar mínimo 1 material", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}