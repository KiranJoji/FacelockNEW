package com.example.facelock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent entertime = new Intent(login.this, mainpage.class);

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
                }
                else {
                    error.setText("Error: Wrong Password");
                    error.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
