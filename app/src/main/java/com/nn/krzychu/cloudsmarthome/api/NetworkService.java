package com.nn.krzychu.cloudsmarthome.api;

import com.nn.krzychu.cloudsmarthome.api.model.SampleRequestModel;
import com.nn.krzychu.cloudsmarthome.api.model.SampleResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NetworkService {

//    String userCredentials = MainActivity.getLogin()+":"+MainActivity.getPassword();
//    String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));
//    URL url = new URL(MainActivity.getAppName() + path);
//    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty ("Authorization", basicAuth);
//            conn.setRequestProperty("Accept", "application/json");



    @GET("matches")
    Call<SampleResponseModel> getSample();

    @Headers("Content-Type: application/json")
    @PUT("counters/{id}")
    Call<SampleResponseModel> putSample(@Path("id") Integer id, @Body SampleRequestModel sample);

    @Headers("Content-Type: application/json")
    @GET("/measurements/getLastTemperature")
    Call<String> getLastTemperature(@Header("Authorization") String authorization);




}
