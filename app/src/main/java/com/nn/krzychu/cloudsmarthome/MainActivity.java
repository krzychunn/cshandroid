package com.nn.krzychu.cloudsmarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nn.krzychu.cloudsmarthome.util.SharedPrefConst;

public class MainActivity extends AppCompatActivity {

    private static String login = "knn";
    private static String password = "nnk";
    private static String appName = "https://arcane-escarpment-70375.herokuapp.com";
    private SharedPreferences sharedPreferences;

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String log) {
        login = log;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String pass) {
        password = pass;
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appN) {
        appName = appN;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //TODO: Usun jezeli nie potrzebujesz
        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(v);
            }
        });
    }

    protected void signIn(View v){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPrefConst.APP_NAME, appName);
        editor.putString(SharedPrefConst.USERNAME, login);
        editor.putString(SharedPrefConst.PASS, password);
        editor.commit();

        //this.login = ((EditText) findViewById(R.id.loginInput)).getText().toString();
        //this.password = ((EditText) findViewById(R.id.passwordInput)).getText().toString();
        //this.appName = ((EditText) findViewById(R.id.appNameInput)).getText().toString();
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
