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

}