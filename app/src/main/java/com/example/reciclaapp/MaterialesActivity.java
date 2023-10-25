package com.example.reciclaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MaterialesActivity extends AppCompatActivity implements MaterialesAdapter.OnItemRemovedListener {
    // 1- AdapterView
    RecyclerView recyclerView;
    // 2- DataSource
    List<MaterialesItem> itemList;
    // 3- Adapters
    MaterialesHeaderAdapter headerAdapter;
    MaterialesAdapter materialesAdapter; // Add MaterialesAdapter
    ConcatAdapter concatAdapter; // Add ConcatAdapter
    private static final int REQUEST_CODE_ACTIVITY2 = 1; // You can use any unique value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiales);

        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        ActivityResultLauncher<Intent> materialesActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        int imageResource = data.getIntExtra("imageResource", 0);
                        String text = data.getStringExtra("text");

                        // Add the chosen material to your data source
                        itemList.add(new MaterialesItem(imageResource, text));

                        // Notify the adapter that an item has been inserted at the last position
                        int insertedPosition = itemList.size() - 1;
                        materialesAdapter.notifyItemInserted(insertedPosition);
                    }
                });

        recyclerView = findViewById(R.id.recyclerViewMateriales);

        // Create a MaterialesHeaderItem instance with the necessary data
        MaterialesHeaderItem headerData = new MaterialesHeaderItem("vie 14 de Aug de 2023", "12:00");

        // Create an instance of MaterialesHeaderAdapter and provide the headerData
        headerAdapter = new MaterialesHeaderAdapter(this, headerData, materialesActivityResultLauncher);

        // Create an instance of MaterialesAdapter (assuming you have a List<MaterialesItem> data)
        itemList= new ArrayList<>(); // Replace with your data

        materialesAdapter = new MaterialesAdapter(this, itemList);

        // Create a ConcatAdapter and add the headerAdapter and materialesAdapter
        concatAdapter = new ConcatAdapter(headerAdapter, materialesAdapter);

        // Create and set the linear layout manager so the recycler view works properly like a scrolling view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the ConcatAdapter as the adapter for the RecyclerView
        recyclerView.setAdapter(concatAdapter);

    }

    @Override
    public void onItemRemoved(int position) {
        // Handle item removal here (e.g., update your data model)
    }

    public void finishMaterialSelection(View v) {
        if (materialesAdapter.getItemCount() > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Debe seleccionar mínimo 1 material",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



}
