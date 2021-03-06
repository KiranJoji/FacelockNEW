package com.example.facelock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //here is the intent to move to new page
        Intent entertime = new Intent(Login.this, ScanFinger.class);

        final EditText enter = findViewById(R.id.password);
        Button loginb = findViewById(R.id.loginbutton);
        TextView error = findViewById(R.id.errorMessage);

        SharedPreferences passlock = PreferenceManager.getDefaultSharedPreferences(this);
        String passDetails = passlock.getString("data", "pass");

        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = enter.getText().toString();

                if(check.equals(passDetails)) {
                    startActivity(entertime);
                    //startActivity is a function in java that runs the intent
                }
                else {
                    error.setText("Error: Wrong Password");
                    error.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
