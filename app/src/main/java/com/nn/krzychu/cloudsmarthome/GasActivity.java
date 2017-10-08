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

public class GasActivity extends AppCompatActivity {

    TextView currentGas = null;
    TextView gasTimestamp = null;
    TextView currentMaxGas = null;
    TextView currentAlarm = null;
    EditText setMaximumGas = null;

    private NetworkService networkService;
    private SharedPreferences sharedPreferences;
    private String appAddress;
    private String username;
    private String pass;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);

        currentGas = (TextView) findViewById(R.id.currentGas);
        gasTimestamp = (TextView) findViewById(R.id.gasTimestamp);
        currentMaxGas = (TextView) findViewById(R.id.currentMaxGas);
        currentAlarm = (TextView) findViewById(R.id.currentAlarm);
        setMaximumGas = (EditText) findViewById(R.id.setMaximumGas);

        sharedPreferences = getSharedPreferences(SharedPrefConst.PREFERENCES_NAME, Activity.MODE_PRIVATE);
        appAddress = sharedPreferences.getString(MainActivity.APP_ADDRESS, "");
        username = sharedPreferences.getString(MainActivity.LOGIN, "");
        pass = sharedPreferences.getString(MainActivity.PASSWORD, "");
        System.out.println("appName=" + appAddress);
        System.out.println("username=" + username);
        System.out.println("pass=" + pass);
        networkService = NetworkServiceProvider.getNetworkService(appAddress);

        networkService.getLastMeasurement(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    currentGas.setText(jsonObject.getString("gasDensity") + " u");
                    gasTimestamp.setText(dateFormat.format((jsonObject.getLong("timeStamp"))));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.generalError, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        networkService.getLastMaximumGas(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    System.out.println("KNN getLastMinimumLight: " + response.body());
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    currentMaxGas.setText(jsonObject.getString("maximumGas") + " u");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.generalError, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        networkService.getLastAlarm(NetClient.getToken(username, pass)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    System.out.println("KNN: " + response.body());
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("state").equals("false")) {
                        currentAlarm.setText(getString(R.string.disabledState));
                    } else if (jsonObject.getString("state").equals("true")) {
                        currentAlarm.setText(getString(R.string.enabledState));
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

        findViewById(R.id.postGasSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("maximumGas", setMaximumGas.getText().toString());

                System.out.println("KNN jsonObject: " + jsonObject);

                networkService.postMinimumLight(NetClient.getToken(username, pass), jsonObject).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }
}
