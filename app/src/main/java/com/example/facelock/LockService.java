package com.example.facelock;

import static com.example.facelock.LockChannel.CHANNEL_ID;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class LockService extends Service {

    UsageStatsManager usageStatsManager;
    ActivityManager activityManager;
    SharedPreferences block;
    MainActivity acquire;
    ArrayList<String> blockedlist;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Your Locking Service is running in the background")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        block = getSharedPreferences("blocklist", MODE_PRIVATE);
        acquire = new MainActivity();
        blockedlist = new ArrayList<String>();

        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                blockedlist = retrieveList(blockedlist);
                Log.d("check","" + blockedlist);
                String app = getTopApp(getApplicationContext(), activityManager);
                app = getAppNameFromPkgName(getApplicationContext(), app);
                Log.d("appname","" + app.trim());
                Log.d("appname","" + blockedlist);
                if (blockedlist.contains(app.trim())) {
                    Log.d("check","dude block has been launched");
                }
            }
        }, 1000, 1000);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            usageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, RestarterLockReciever.class);
        this.sendBroadcast(broadcastIntent);
    }

    public String getTopApp(@NonNull Context context, @NonNull ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        }
        else {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    Log.d("result", "Result: " + result);
                    result = event.getPackageName();
                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                Log.d("result", "Result: " + result);
                return result;
            }
        }
        return "";
    }

    public static String getAppNameFromPkgName(Context context, String packageName) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> installed = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        for (ResolveInfo ri : installed) {
            if (ri.activityInfo != null && packageName.equals(ri.activityInfo.packageName.toString())) {
                    return ri.activityInfo.applicationInfo.loadLabel(
                            context.getPackageManager()).toString();
            }
        }
        return packageName;
    }

    public ArrayList<String> retrieveList(ArrayList<String> listblock) {
        Set<String> set = block.getStringSet("blocklist", null);
        if(set != null) {
            listblock.addAll(set);
            Log.d("retrievesharedPref","retrieved "+listblock);
            return listblock;
        }
        else {
            return new ArrayList<String>();
        }
    }
}