package com.mpo.weather;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView city;
    LinearLayout layout;
    Button button;
    EditText editText;
    TextView Weather;
    TextView Temp;
    TextView Speed;
    TextView Humidity;
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(receiver, new IntentFilter(GisService.CHANNEL));
        city = findViewById(R.id.city);
        layout = findViewById(R.id.imagelayout);
        editText = findViewById(R.id.edit);
        button = findViewById(R.id.button);
        Temp = findViewById(R.id.Temp);
        Weather = findViewById(R.id.Weather);
        Speed = findViewById(R.id.Speed);
        Humidity = findViewById(R.id.Humidity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() > 0) {
                    httpRequest.CITY = editText.getText().toString();
                    editText.getText().clear();
                    editText.setClickable(false);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    Intent intent = new Intent(MainActivity.this, GisService.class);
                    startService(intent);
                }
                else Toast.makeText(MainActivity.this, "Введите город",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, GisService.class);
        stopService(intent);
        super.onDestroy();
    }
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(GisService.INFO);
            try {
                JSONObject start = new JSONObject(response);
                JSONObject main = start.getJSONObject("main");
                JSONObject wind = start.getJSONObject("wind");
                JSONObject marlena = start.getJSONArray("weather").getJSONObject(0);

                String temp = main.getString("temp");
                String description = marlena.getString("description");
                String speed = wind.getString("speed");
                String humidity = main.getString("humidity");
                city.setText(httpRequest.CITY);
                Temp.setText(temp + "°C");
                Weather.setText(description);
                Speed.setText(speed + " m/s");
                Humidity.setText(humidity + "%");
                if (description.contains("rain")){
                    layout.setBackgroundResource(R.drawable.rain);
                }
                else if (description.contains("clear")){
                    layout.setBackgroundResource(R.drawable.clear);
                }
                else if (description.contains("cloud")){
                    layout.setBackgroundResource(R.drawable.clouds);
                }
                else if (description.contains("snow")){
                    layout.setBackgroundResource(R.drawable.snow);
                }
                else layout.setBackgroundColor(getColor(R.color.BLUE));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };
}