package com.greencarson.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TutorialActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial3);
    }

    public void goBack(View v){
        Intent intent = new Intent(this, TutorialActivity2.class);
        startActivity(intent);
    }

    public void goForward(View v){
        Intent intent = new Intent(this, TutorialActivity4.class);
        startActivity(intent);
    }
}