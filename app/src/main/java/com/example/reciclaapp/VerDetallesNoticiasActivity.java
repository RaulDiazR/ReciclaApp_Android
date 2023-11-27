package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class VerDetallesNoticiasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles_noticias);

        // Busque la barra de herramientas por su ID y configúrela como barra de aplicaciones
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Eliminar el título predeterminado de la barra de aplicaciones
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Habilitar el botón Atrás (navegación hacia arriba)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        // Retrieve data from intent extras
        Intent intent = getIntent();
        String tituloTxt = intent.getStringExtra("titulo");
        String cuerpoTxt = intent.getStringExtra("cuerpo");
        String autorTxt = intent.getStringExtra("autor");
        String fotoUrl = intent.getStringExtra("fotoUrl");

        ImageView imgNoticia = findViewById(R.id.imagen);
        TextView titulo = findViewById(R.id.titulo);
        TextView cuerpo = findViewById(R.id.cuerpo);
        TextView autor = findViewById(R.id.autor);

        Picasso.get().load(fotoUrl).placeholder(R.drawable.icon_loading).error(R.drawable.socio1).into(imgNoticia);
        titulo.setText(tituloTxt);
        cuerpo.setText(cuerpoTxt);
        autor.setText(autorTxt);
    }

    // Método llamado cuando se presiona el botón de navegación hacia arriba
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}