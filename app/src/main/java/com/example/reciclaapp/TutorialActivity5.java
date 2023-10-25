package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TutorialActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial5);
    }

    public void goBack(View v){
        Intent intent = new Intent(this, TutorialActivity4.class);
        startActivity(intent);
    }

    public void Continuar(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}