package com.nn.krzychu.cloudsmarthome.util;

/**
 * Created by Krzychu on 09.03.2017.
 */

import android.util.Base64;

import com.nn.krzychu.cloudsmarthome.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetClient{


    public static String getToken(String username, String pass){
        String userCredentials = username+":"+pass;
        return "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));
    }

/*    public String get(String path) {

        String output = null;

        try {
            String userCredentials = MainActivity.getLogin()+":"+MainActivity.getPassword();
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));
            URL url = new URL(MainActivity.getAppName() + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty ("Authorization", basicAuth);
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            output = br.readLine();

            conn.disconnect();

            return output;

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return output;

    }*/
}