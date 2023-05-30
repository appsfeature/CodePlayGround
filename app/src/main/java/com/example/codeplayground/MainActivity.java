package com.example.codeplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.codeplayground.network.FBNetworkManager;
import com.example.codeplayground.network.FBNetworkModel;
import com.example.codeplayground.network.FormResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openLifecycleActivity(View view) {
//        startActivity(new Intent(this, LifecycleActivity.class));
        FBNetworkManager nm = new FBNetworkManager(this, "");
        nm.testApi(new FormResponse.Callback<FBNetworkModel>() {
            @Override
            public void onSuccess(FBNetworkModel response) {
                Log.d("onSuccess",response.getMessage());
            }
        });
    }
}