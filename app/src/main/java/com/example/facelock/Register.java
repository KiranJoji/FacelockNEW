package com.example.facelock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    private final String KEY_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isAccessGrantedUsage()) {
            Intent setting = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(setting);
        }
        if (!isAccessGrantedDraw()) {
            Intent draw = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(draw);
        }
        setContentView(R.layout.register);

        Intent logintime = new Intent(Register.this, Login.class);

        EditText pass1 = findViewById(R.id.passwordtry);
        EditText pass2 = findViewById(R.id.passwordtry1);
        Button register = findViewById(R.id.registerbutton);

        SharedPreferences passlock = PreferenceManager.getDefaultSharedPreferences(this);
        String passDetails = passlock.getString("data", "null");
        if(!(passDetails.equals("null") )) {
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

    private boolean isAccessGrantedUsage() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean isAccessGrantedDraw() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}