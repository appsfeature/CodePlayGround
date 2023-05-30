package com.example.codeplayground;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class LifecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);
        Log.d("@Test-LifeActivity", "onCreate invoked");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("@Test-LifeActivity", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("@Test-LifeActivity", "onResume invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("@Test-LifeActivity", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("@Test-LifeActivity", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("@Test-LifeActivity", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("@Test-LifeActivity", "onDestroy invoked");
    }

    public void openSecondActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}