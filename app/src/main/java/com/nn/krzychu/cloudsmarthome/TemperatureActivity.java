package com.nn.krzychu.cloudsmarthome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nn.krzychu.cloudsmarthome.api.NetworkService;
import com.nn.krzychu.cloudsmarthome.api.NetworkServiceProvider;
import com.nn.krzychu.cloudsmarthome.util.NetClient;
import com.nn.krzychu.cloudsmarthome.util.SharedPrefConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemperatureActivity extends AppCompatActivity {

    TextView currentTemperature = null;
    TextView temperatureTimestamp = null;
    TextView currentMinTemperature = null;
    TextView currentMaxTemperature = null;
    TextView currentAirCondition = null;
    TextView currentHeating = null;
    EditText setMinimumTemperature = null;
    EditText setMaximumTemperature = null;

    private NetworkService networkService;
    private SharedPreferences sharedPreferences;
    private String appAddress;
    private String username;
    private String pass;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        getLastMeasurements();

        findViewById(R.id.postTemperatureSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setMinimumTemperature.getText() != null && setMaximumTemperature.getText() != null) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("minimumTemperature", setMinimumTemperature.getText().toString());
                    jsonObject.addProperty("maximumTemperature", setMaximumTemperature.getText().toString());

                    networkService.postOptimalTemperature(NetClient.getToken(username, pass), jsonObject).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            getLastMeasurements();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            getLastMeasurements();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), R.string.dataFormatError, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLastMeasurements() {
        currentTemperature = (TextView) findViewById(R.id.currentTemperature);
        temperatureTimestamp = (TextView) findViewById(R.id.temperatureTimestamp);
        currentMinTemperature = (TextView) findViewById(R.id.currentMinTemp);
        currentMaxTemperature = (TextView) findViewById(R.id.currentMaxTemp);
        currentAirCondition = (TextView) findViewById(R.id.currentAirCondition);
        currentHeating = (TextView) findViewById(R.id.currentHeating);
        setMinimumTemperature = (EditText) findViewById(R.id.setMinimumTemperature);
        setMaximumTemperature = (EditText) findViewById(R.id.setMaximumTemperature);

        sharedPreferences = getSharedPreferences(SharedPrefConst.PREFERENCES_NAME, Activity.MODE_PRIVATE);
        appAddress = sharedPreferences.getString(MainActivity.APP_ADDRESS, "");
        username = sharedPreferences.getString(MainActivity.LOGIN, "");
        pass = sharedPreferences.getString(MainActivity.PASSWORD, "");
        networkService = NetworkServiceProvider.getNetworkService(appAddress);

        networkService.getLastMeasurement(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    currentTemperature.setText(jsonObject.getString("temperature") + " °C");
                    temperatureTimestamp.setText(dateFormat.format((jsonObject.getLong("timeStamp"))));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.generalError, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        networkService.getLastOptimalTemperature(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    currentMinTemperature.setText(jsonObject.getString("minimumTemperature") + " °C");
                    currentMaxTemperature.setText(jsonObject.getString("maximumTemperature") + " °C");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.generalError, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        networkService.getLastAirCondition(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("state").equals("false")) {
                        currentAirCondition.setText(getString(R.string.disabledState));
                    } else if (jsonObject.getString("state").equals("true")) {
                        currentAirCondition.setText(getString(R.string.enabledState));
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.generalError, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        networkService.getLastHeating(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("state").equals("false")) {
                        currentHeating.setText(getString(R.string.disabledState));
                    } else if (jsonObject.getString("state").equals("true")) {
                        currentHeating.setText(getString(R.string.enabledState));
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.generalError, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
}
