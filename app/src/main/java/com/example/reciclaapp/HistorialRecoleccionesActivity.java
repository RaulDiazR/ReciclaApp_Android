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
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.shape_green_square, null);
        Drawable drawable2 = ResourcesCompat.getDrawable(res, R.drawable.shape_red_square, null);
        Drawable drawable3 = ResourcesCompat.getDrawable(res, R.drawable.shape_gray_square, null);
        Drawable drawable4 = ResourcesCompat.getDrawable(res, R.drawable.shape_gold_square, null);

        int color = ResourcesCompat.getColor(res, R.color.green, null);
        int color2 = ResourcesCompat.getColor(res, R.color.red, null);
        int color3 = ResourcesCompat.getColor(res, R.color.gray, null);
        int color4 = ResourcesCompat.getColor(res, R.color.pink, null);

        HistorialItem item1 = new HistorialItem(drawable3, "25/09/2023", "11:10","2 materiales","Iniciada", color3);
        HistorialItem item2 = new HistorialItem(drawable4, "25/09/2023", "11:10","3 materiales","En Proceso", color4);
        HistorialItem item3 = new HistorialItem(drawable, "25/09/2023", "11:10","5 materiales","Completada", color);
        HistorialItem item4 = new HistorialItem(drawable2, "29/09/2023", "11:08","2 materiales","Cancelada", color2);
        HistorialItem item5 = new HistorialItem(drawable3, "25/09/2023", "11:10","4 materiales","Completada", color3);
        HistorialItem item6 = new HistorialItem(drawable, "30/09/2023", "15:08","1 material","Completada", color);

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
                Toast.LENGTH_SHORT).show();
    }
}