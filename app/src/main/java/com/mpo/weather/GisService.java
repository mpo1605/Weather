package com.mpo.weather;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GisService extends Service {
    public static final String CHANNEL = "GIS_SERVICE";
    public static final String INFO = "INFO";
    private Handler h;

    @Override
    public void onCreate() {
        super.onCreate();
        h = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String response = (String) msg.obj;
                Intent i = new Intent(CHANNEL);
                i.putExtra(INFO, response);
                sendBroadcast(i);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread weather = new Thread(new httpRequest(h));
        weather.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
