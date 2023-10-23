package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistorialRecoleccionesActivity extends AppCompatActivity implements HistorialItemClickListener {

    // 1- AdapterView
    RecyclerView recyclerView;

    // 2- DataSource
    List<HistorialItem> itemList;

    // 3- Adapter
    HistorialAdapter historialAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_recolecciones);

        // Find the Toolbar by its ID and et the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewHistorial);

        itemList = new ArrayList<>();
        Resources res = getResources();
        Drawable green_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_green, null);
        Drawable red_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_red, null);
        Drawable gray_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_gray, null);
        Drawable gold_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_gold, null);
        Drawable blue_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_blue, null);

        int green = ResourcesCompat.getColor(res, R.color.green, null);
        int red = ResourcesCompat.getColor(res, R.color.red, null);
        int gray = ResourcesCompat.getColor(res, R.color.gray, null);
        int gold = ResourcesCompat.getColor(res, R.color.golden, null);
        int blue = ResourcesCompat.getColor(res, R.color.blue, null);

        HistorialItem item1 = new HistorialItem(gray_square, "25/09/2023", "14:10","2 materiales","Pendiente", gray);
        HistorialItem item2 = new HistorialItem(gold_square, "25/09/2023", "16:10","3 materiales","Iniciada", gold);
        HistorialItem item3 = new HistorialItem(blue_square, "25/09/2023", "10:10","5 materiales","En Proceso", blue);
        HistorialItem item4 = new HistorialItem(green_square, "29/09/2023", "09:08","2 materiales","Completada", green);
        HistorialItem item5 = new HistorialItem(red_square, "25/09/2023", "15:10","4 materiales","Cancelada", red);
        HistorialItem item6 = new HistorialItem(green_square, "30/09/2023", "13:08","1 material","Completada", green);

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);
        itemList.add(item6);

        // create and set the linear layout manager so the recycler view works properly like a scrolling view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historialAdapter = new HistorialAdapter(itemList);
        recyclerView.setAdapter(historialAdapter);

        historialAdapter.setClickListener(this);

    }

    public void addOrder (View v) {
        Intent intent = new Intent(this, OrdenHorarioActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v, int pos) {
        Toast.makeText(this,
                "You choose this: " + itemList.get(pos).getEstado(),
                Toast.LENGTH_LONG).show();
    }
}