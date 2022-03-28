package com.example.facelock;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

public class mainpage extends AppCompatActivity {
    TextView text;
    ListView listView;
    ConstraintLayout mainpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        listView = findViewById(R.id.listview);
        text = findViewById(R.id.totalapp);
    }

    public void getallapps(View view) throws PackageManager.NameNotFoundException {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // get list of all the apps installed
        List<ResolveInfo> installed = getPackageManager().queryIntentActivities(mainIntent, 0);
        String name;

        // get size of installed list and create a list
        List<String> apps = new ArrayList<String>();
        for (ResolveInfo ri : installed) {
            if (ri.activityInfo != null) {
                // get package
                Resources res = getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);
                // if activity label res is found
                if (ri.activityInfo.labelRes != 0) {
                    name = res.getString(ri.activityInfo.labelRes);
                } else {
                    name = ri.activityInfo.applicationInfo.loadLabel(
                            getPackageManager()).toString();
                }
                apps.add(name);
            }
        }
        java.util.Collections.sort(apps);
        // set all the apps name in list view
        listView.setAdapter(new ArrayAdapter<String>(mainpage.this, android.R.layout.simple_list_item_1, apps));
        // write total count of apps available.
        text.setText(installed.size() + " Apps are installed");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
// the getLaunchIntentForPackage returns an intent that you can use with startActivity()
}
