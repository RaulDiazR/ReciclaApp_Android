package com.greencarson.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TutorialActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);
    }

    public void goBack(View v){
        Intent intent = new Intent(this, TutorialActivity1.class);
        startActivity(intent);
    }

    public void goForward(View v){
        Intent intent = new Intent(this, TutorialActivity3.class);
        startActivity(intent);
    }
}