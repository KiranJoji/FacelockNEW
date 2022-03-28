package com.example.facelock;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class mainpage extends AppCompatActivity {
    TextView text;
    ListView listView;
    List<String> apps;
    Button face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        listView = findViewById(R.id.listview);
        text = findViewById(R.id.totalapp);
        face = findViewById(R.id.facebutton);

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent faceit = new Intent(mainpage.this, facelay.class);
                startActivity(faceit);
            }
        });
    }

    public void getallapps(View view) throws PackageManager.NameNotFoundException {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // get list of all the apps installed
        List<ResolveInfo> installed = getPackageManager().queryIntentActivities(mainIntent, 0);
        String name;

        // get size of installed list and create a list
        apps = new ArrayList<String>();
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
        listView.setAdapter(new MyListAdapter(this, R.layout.applistlayout, apps));
        // write total count of apps available.
        text.setText(installed.size() + " Apps are installed");
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.applistname = (TextView) convertView.findViewById(R.id.appname);
                viewHolder.applistswitch = (Switch) convertView.findViewById(R.id.appswitch);
                viewHolder.applistswitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT);
                    }
                });
                convertView.setTag(viewHolder);
            }
            else {
                mainViewholder = (ViewHolder) convertView.getTag();
                mainViewholder.applistname.setText(getItem(position));
            }

            return convertView;
        }
    }

    public class ViewHolder {
        Switch applistswitch;
        TextView applistname;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
// the getLaunchIntentForPackage returns an intent that you can use with startActivity()
}
