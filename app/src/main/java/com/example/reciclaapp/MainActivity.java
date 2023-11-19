package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonToMap = findViewById(R.id.buttonToMap);

        buttonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivityMap();
            }
        });
    }

    public void OpenActivityMap() {

        try {

            Intent intent = new Intent(MainActivity.this, StreetMapActivity.class);
            intent.putExtra("postalcode", "72080");
            intent.putExtra("state", "Puebla");
            intent.putExtra("city", "Municipio de Puebla");
            intent.putExtra("country", "Mexico");
            intent.putExtra("street", "Calle 34 Poniente");
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá el error en el Log
            Toast.makeText(this, "Error al obtener el cliente de ubicación.", Toast.LENGTH_SHORT).show();
        }

    }


}