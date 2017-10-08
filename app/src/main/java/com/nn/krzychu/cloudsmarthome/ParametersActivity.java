package com.nn.krzychu.cloudsmarthome;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Krzychu on 22.09.2017.
 */

public class ParametersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
    }

    public void openTemperatureWindow(View v){
        Intent temperature = new Intent(this, TemperatureActivity.class);
        startActivity(temperature);
    }

    public void openHumidityWindow(View v){
        Intent humidity = new Intent(this, HumidityActivity.class);
        startActivity(humidity);
    }

    public void openLightWindow(View v){
        Intent light = new Intent(this, LightActivity.class);
        startActivity(light);
    }

    public void openGasWindow(View v){
        Intent gas = new Intent(this, GasActivity.class);
        startActivity(gas);
    }
}
