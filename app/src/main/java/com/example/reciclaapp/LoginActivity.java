package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToRegistro(View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void Continuar (View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}