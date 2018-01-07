package com.nn.krzychu.cloudsmarthome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class HumidityActivity extends AppCompatActivity {

    TextView currentHumidity = null;
    TextView humidityTimestamp = null;
    TextView currentMinHumidity = null;
    TextView currentHumidifier = null;
    EditText setMinimumHumidity = null;

    private NetworkService networkService;
    private SharedPreferences sharedPreferences;
    private String appAddress;
    private String username;
    private String pass;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);

        getLastMeasurements();

        findViewById(R.id.postHumiditySettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setMinimumHumidity.getText().toString().trim().length() != 0 ) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("minimumHumidity", setMinimumHumidity.getText().toString());

                    networkService.postMinimumHumidity(NetClient.getToken(username, pass), jsonObject).enqueue(new Callback<String>() {
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
        currentHumidity = (TextView) findViewById(R.id.currentHumidity);
        humidityTimestamp = (TextView) findViewById(R.id.humidityTimestamp);
        currentMinHumidity = (TextView) findViewById(R.id.currentMinHumidity);
        currentHumidifier = (TextView) findViewById(R.id.currentHumidifier);
        setMinimumHumidity = (EditText) findViewById(R.id.setMinimumHumidity);

        sharedPreferences = getSharedPreferences(SharedPrefConst.PREFERENCES_NAME, Activity.MODE_PRIVATE);
        appAddress = sharedPreferences.getString(MainActivity.APP_ADDRESS, null);
        username = sharedPreferences.getString(MainActivity.LOGIN, null);
        pass = sharedPreferences.getString(MainActivity.PASSWORD, null);
        networkService = NetworkServiceProvider.getNetworkService(appAddress);

        networkService.getLastMeasurement(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    currentHumidity.setText(jsonObject.getString("humidity") + " %");
                    humidityTimestamp.setText(dateFormat.format((jsonObject.getLong("timeStamp"))));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.generalError, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        networkService.getLastMinimumHumidity(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    currentMinHumidity.setText(jsonObject.getString("minimumHumidity") + " %");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.generalError, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        networkService.getLastHumidifier(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("state").equals("false")) {
                        currentHumidifier.setText(getString(R.string.disabledState));
                    } else if (jsonObject.getString("state").equals("true")) {
                        currentHumidifier.setText(getString(R.string.enabledState));
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
