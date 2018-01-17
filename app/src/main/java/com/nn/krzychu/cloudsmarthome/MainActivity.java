package com.nn.krzychu.cloudsmarthome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nn.krzychu.cloudsmarthome.api.NetworkService;
import com.nn.krzychu.cloudsmarthome.api.NetworkServiceProvider;
import com.nn.krzychu.cloudsmarthome.util.NetClient;
import com.nn.krzychu.cloudsmarthome.util.SharedPrefConst;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static String login;
    private static String password;
    private static String appName;
    private static String appAddress;
    public static String LOGIN = "username";
    public static String PASSWORD = "password";
    public static String APP_NAME = "app_name";
    public static String APP_ADDRESS = "app_address";
    private SharedPreferences sharedPreferences;
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);

        sharedPreferences = getSharedPreferences(SharedPrefConst.PREFERENCES_NAME, Activity.MODE_PRIVATE);
        this.login = sharedPreferences.getString(LOGIN, null);
        this.password = sharedPreferences.getString(PASSWORD, null);
        this.appName = sharedPreferences.getString(APP_NAME, null);
        this.appAddress = sharedPreferences.getString(APP_ADDRESS, null);
        if(login != null) {
            ((EditText) findViewById(R.id.setLogin)).setText(login);
        }
        if(password != null) {
            ((EditText) findViewById(R.id.setPassword)).setText(password);
        }
        if(appName != null) {
            ((EditText) findViewById(R.id.setAppName)).setText(appName);
        }
    }

    protected void signIn(final View v){

        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        this.login = ((EditText) findViewById(R.id.setLogin)).getText().toString();
        this.password = ((EditText) findViewById(R.id.setPassword)).getText().toString();
        this.appName = ((EditText) findViewById(R.id.setAppName)).getText().toString();
        preferencesEditor.putString(APP_NAME, this.appName).commit();
        preferencesEditor.apply();
        preferencesEditor.putString(APP_ADDRESS, SharedPrefConst.HTTPS + sharedPreferences.getString(APP_NAME, null) + SharedPrefConst.HEROKU).commit();
        preferencesEditor.putString(LOGIN, this.login).commit();
        preferencesEditor.putString(PASSWORD, this.password).commit();
        preferencesEditor.apply();

        networkService = NetworkServiceProvider.getNetworkService(sharedPreferences.getString(APP_ADDRESS, null));

        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setTitle(R.string.logIn);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        networkService.getLastMeasurement(NetClient.getToken(sharedPreferences.getString(LOGIN, null), sharedPreferences.getString(PASSWORD, null))).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    jsonObject.getLong("timeStamp");
                    progressDialog.dismiss();
                    openParametersWindow(v);
                } catch (Exception e) {
                    login=null;
                    password=null;
                    appName=null;
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_LONG).show();
            }
        });

    }


    public void openParametersWindow(View v){
        Intent parameters = new Intent(this, ParametersActivity.class);
        startActivity(parameters);
    }

}
