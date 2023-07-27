package com.example.codeplayground;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "@Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, YoutubePlayerActivity.class));
        finish();
    }

    public void openLifecycleActivity(View view) {
//        startActivity(new Intent(this, LifecycleActivity.class));
//        FBNetworkManager nm = new FBNetworkManager(this, "");
//        nm.testApi(new FormResponse.Callback<FBNetworkModel>() {
//            @Override
//            public void onSuccess(FBNetworkModel response) {
//                Log.d("onSuccess",response.getMessage());
//            }
//        });
//        boolean isRunning = isJobServiceOn(this);
//        onStartJobIntentService();
//        boolean isRunning = isRunningServices(ForegroundServiceJava.class);
//        Log.d(TAG, "Services : isRunning - " + isRunning);

        throw new RuntimeException("Test Crash"); // Force a crash
    }

    public void onStartJobIntentService() {
//        Intent mIntent = new Intent(this, MyJobIntentService.class);
//        MyJobIntentService.enqueueWork(this, mIntent);
//        startService(new Intent( this, NewService.class ) );
        ForegroundServiceJava.startService(this);
    }
}