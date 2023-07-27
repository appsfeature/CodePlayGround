package com.example.codeplayground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private EditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d("@Test-SecondActivity", "onCreate invoked");

        etInput = findViewById(R.id.et_input);
        findViewById(R.id.btn_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, getIntent().putExtra("test", etInput.getText().toString()));
                finish();
            }
        });

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            getUserInputData(this, intent);
        }
    }

    private void getUserInputData(Context context, Intent intent) {
        if(intent != null) {
            if (intent.getData() != null && intent.getData().getAuthority().equals(context.getString(R.string.url_public_domain_host_manifest))) {
                //registerDynamicLinks(activity);
            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, getIntent());
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("@Test-SecondActivity", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("@Test-SecondActivity", "onResume invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("@Test-SecondActivity", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("@Test-SecondActivity", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("@Test-SecondActivity", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("@Test-SecondActivity", "onDestroy invoked");
    }
}