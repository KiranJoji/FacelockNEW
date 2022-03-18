package com.example.facelock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class register extends AppCompatActivity {

    private final String KEY_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Intent logintime = new Intent(register.this, login.class);

        EditText pass1 = findViewById(R.id.passwordtry);
        EditText pass2 = findViewById(R.id.passwordtry1);
        Button register = findViewById(R.id.registerbutton);

        SharedPreferences passlock = PreferenceManager.getDefaultSharedPreferences(this);
        String passDetails = passlock.getString("data", "null");
        if(!( passDetails.equals("null") )) {
            startActivity(logintime);
        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = pass1.getText().toString();
                if (pass1.getText().toString().equals(pass2.getText().toString()) && !(pass.isEmpty())) {

                    SharedPreferences.Editor editor = passlock.edit();

                    editor.putString("data", pass);
                    editor.apply();

                    startActivity(logintime);
                }
            }
        });
    }
}