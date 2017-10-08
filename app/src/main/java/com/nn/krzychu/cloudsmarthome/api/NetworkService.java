package com.nn.krzychu.cloudsmarthome.api;

import com.nn.krzychu.cloudsmarthome.api.model.SampleRequestModel;
import com.nn.krzychu.cloudsmarthome.api.model.SampleResponseModel;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NetworkService {

    @GET("matches")
    Call<SampleResponseModel> getSample();

    @Headers("Content-Type: application/json")
    @PUT("counters/{id}")
    Call<SampleResponseModel> putSample(@Path("id") Integer id, @Body SampleRequestModel sample);

    @Headers("Content-Type: application/json")
    @GET("measurements/getLastTimestamp")
    Call<String> getLastTimestamp(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @GET("measurements/getLast")
    Call<Object> getLastMeasurement(@Header("Authorization") String authorization);


    @Headers("Content-Type: application/json")
    @GET("optimalTemperature/getLast")
    Call<Object> getLastOptimalTemperature(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @GET("airCondition/getLast")
    Call<Object> getLastAirCondition(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @GET("heating/getLast")
    Call<Object> getLastHeating(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @POST("optimalTemperature/add")
    Call<String> postOptimalTemperature(@Header("Authorization") String authorization, @Body JsonObject  body);


    @Headers("Content-Type: application/json")
    @GET("minimumHumidity/getLast")
    Call<Object> getLastMinimumHumidity(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @GET("humidifier/getLast")
    Call<Object> getLastHumidifier(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @POST("minimumHumidity/add")
    Call<String> postMinimumHumidity(@Header("Authorization") String authorization, @Body JsonObject  body);


    @Headers("Content-Type: application/json")
    @GET("minimumLight/getLast")
    Call<Object> getLastMinimumLight(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @GET("light/getLast")
    Call<Object> getLastLighting(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @POST("minimumLight/add")
    Call<String> postMinimumLight(@Header("Authorization") String authorization, @Body JsonObject  body);


    @Headers("Content-Type: application/json")
    @GET("maximumGas/getLast")
    Call<Object> getLastMaximumGas(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @GET("buzzer/getLast")
    Call<Object> getLastAlarm(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @POST("maximumGas/add")
    Call<String> postMaximumGas(@Header("Authorization") String authorization, @Body JsonObject  body);


}
