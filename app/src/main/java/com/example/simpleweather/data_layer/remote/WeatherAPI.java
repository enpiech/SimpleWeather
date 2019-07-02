package com.example.simpleweather.data_layer.remote;

import com.example.simpleweather.data_layer.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("/data/2.5/forecast?units=metric&lang=vi")
    Call<WeatherResponse> getWeatherForecastByCityName(@Query("q") String cityName, @Query("appid") String app_id);
}
