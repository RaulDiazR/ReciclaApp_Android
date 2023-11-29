package com.greencarson.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TutorialActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial4);
    }

    public void goBack(View v){
        Intent intent = new Intent(this, TutorialActivity3.class);
        startActivity(intent);
    }

    public void goForward(View v){
        Intent intent = new Intent(this, TutorialActivity5.class);
        startActivity(intent);
    }
}