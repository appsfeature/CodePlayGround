package com.example.codeplayground;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.droidhelios.playstoretesting.IRemoteService;
import com.droidhelios.playstoretesting.RemoteCallback;

import java.util.List;

/**
 * @apiNote : Copy AIDL Folder
 */
public class AIDLUsageInAnotherApp extends AppCompatActivity {

    private Button btnAction;

    private IRemoteService mRemoteHandler;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mRemoteHandler = IRemoteService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void showStatus() {
        try {
            Toast.makeText(AIDLUsageInAnotherApp.this, "Status : " + mRemoteHandler.getStatus() + " PID = " + mRemoteHandler.getPid(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
                    Toast.makeText(AIDLUsageInAnotherApp.this, "Service not started yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_example);

        btnAction = findViewById(R.id.btn_action);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatus();
            }
        });
        findViewById(R.id.btn_action2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRunningServices();
            }
        });
        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mRemoteHandler != null){
                        mRemoteHandler.openRequest(new RemoteCallback.Stub() {
                            @Override
                            public void onSuccess(String result) throws RemoteException {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(AIDLUsageInAnotherApp.this, result, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(int errorCode) throws RemoteException {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(AIDLUsageInAnotherApp.this, "ErrorCode : " + errorCode, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }else {
                        Toast.makeText(AIDLUsageInAnotherApp.this, "Service not started yet", Toast.LENGTH_SHORT).show();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.btn_action3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartActivity();
            }
        });

    }

    private void restartActivity() {
        Intent myIntent = new Intent(this, AIDLUsageInAnotherApp.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent);
    }

    private void listRunningServices() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = manager.getRunningServices(Integer.MAX_VALUE);
        if(serviceList.size() > 0) {
            Log.d("RemoteService", "-------------------------------------------------");
            for (ActivityManager.RunningServiceInfo service : serviceList) {
                Log.d("RemoteService", "Services : " + service.service.getClassName());
            }
            Log.d("RemoteService", "-------------------------------------------------");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent("com.aidlfeature.BIND");
        intent.setPackage("com.droidhelios.playstoretesting");
        // or
        // use in manifest '<category android:name="android.intent.category.DEFAULT" />'
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }
}
