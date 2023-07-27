package com.example.codeplayground;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.codeplayground.network.TaskRunner;

import java.util.concurrent.Callable;

public class MyJobIntentService extends JobIntentService {

    public static final int JOB_ID = 1;
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d("@Test", "Services : onHandleWork()");

        TaskRunner.getInstance().executeAsync(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(5000);
                return null;
            }
        }, new TaskRunner.Callback<Object>() {
            @Override
            public void onComplete(Object result) {
                Toast.makeText(MyJobIntentService.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d("@Test", "Services : onStopCurrentWork()");
        return super.onStopCurrentWork();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("@Test", "Services : onDestroy()");
    }
}