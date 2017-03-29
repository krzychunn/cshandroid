package com.nn.krzychu.cloudsmarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import utils.NetClient;

public class TemperatureActivity extends AppCompatActivity {

    TextView currentTemperature;
    TextView temperatureTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
    }

    protected void loadData(View v){
        new NetTask(this).execute("/measurements/getLastTemperature", "/measurements/getLastTimestamp", "/optimalTemperature/getLast", "/heating/getLast", "/airCondition/getLast");
        currentTemperature = ((TextView) (findViewById(R.id.temperature)));
        temperatureTimestamp = ((TextView) (findViewById(R.id.temperatureTimestamp)));
    }

    private class NetTask extends AsyncTask<String, Void, String[]>{

        public NetTask(TemperatureActivity temperatureActivityContext) {
            this.temperatureActivityContext = temperatureActivityContext;
        }

        private Context temperatureActivityContext = null;

        @Override
        protected String[] doInBackground(String... strings) {
            String []values = new String[6];
            NetClient netClient = new NetClient();
            values[0] = netClient.get(strings[0]);
            values[1] = netClient.get(strings[1]);
            return values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] values) {

            currentTemperature.setText(values[0]);
            temperatureTimestamp.setText((new Date(Long.parseLong(values[1]))).toString());
            super.onPostExecute(values);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String[] s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
