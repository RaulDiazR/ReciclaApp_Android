package com.greencarson.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class InicioActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        mAuth = FirebaseAuth.getInstance();
    }

    public void Iniciar(View v){
        Intent intent = new Intent(this, TutorialActivity1.class);
        startActivity(intent);
    }
}