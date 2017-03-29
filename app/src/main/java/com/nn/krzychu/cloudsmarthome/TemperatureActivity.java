package com.nn.krzychu.cloudsmarthome;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nn.krzychu.cloudsmarthome.api.NetworkService;
import com.nn.krzychu.cloudsmarthome.api.NetworkServiceProvider;
import com.nn.krzychu.cloudsmarthome.util.NetClient;
import com.nn.krzychu.cloudsmarthome.util.SharedPrefConst;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemperatureActivity extends AppCompatActivity {

    TextView currentTemperature;
    TextView temperatureTimestamp;

    private NetworkService networkService;
    private SharedPreferences sharedPreferences;
    private String username;
    private String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String appNAme = sharedPreferences.getString(SharedPrefConst.APP_NAME,"");
        username = sharedPreferences.getString(SharedPrefConst.USERNAME,"");
        pass = sharedPreferences.getString(SharedPrefConst.PASS,"");
        networkService = NetworkServiceProvider.getNetworkService(appNAme);

        //TODO: Usun jezeli nie potrzebujesz - ja to dodalem bo nie dziala z jakiegos powodu bindowanie z XML - stawiam na zasieg metody loadData
        findViewById(R.id.loadData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadData(v);
                networkService.getLastTemperature(NetClient.getToken(username,pass)).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        currentTemperature.setText(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
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
