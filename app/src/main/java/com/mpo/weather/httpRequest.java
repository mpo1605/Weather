package com.mpo.weather;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class httpRequest implements Runnable{
    public Handler handler;
    public static String CITY = "Moscow";
    public static String KEY = "48ac37d3123b615df169f099a78838e8";
    private URL url;
    public httpRequest(Handler h){
        this.handler = h;
        try {
            this.url =new URL("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&appid=" + KEY + "&units=metric");
        } catch (MalformedURLException e) {
            throw new RuntimeException();
        }
    }
    @Override
    public void run() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Scanner in = new Scanner(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while (in.hasNext()) {
                response.append(in.nextLine());
            }

            in.close();
            connection.disconnect();
            Message msg = Message.obtain();
            msg.obj = response.toString();
            handler.sendMessage(msg);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}