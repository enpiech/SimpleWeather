package com.example.simpleweather.data_layer.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.simpleweather.data_layer.model.WeatherResponse;
import com.example.simpleweather.data_layer.remote.RetrofitService;
import com.example.simpleweather.data_layer.remote.WeatherAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private static WeatherRepository sWeatherRepository;

    public static WeatherRepository getInstance() {
        if (sWeatherRepository == null) {
            sWeatherRepository = new WeatherRepository();
        }
        return sWeatherRepository;
    }

    private final String BASE_URL = "https://api.openweathermap.org/";
    private WeatherAPI mWeatherAPI;
    private static MutableLiveData<WeatherResponse> mData = new MutableLiveData<>();

    private WeatherRepository() {
        this.mWeatherAPI = RetrofitService.createService(WeatherAPI.class, BASE_URL);
    }

    public MutableLiveData<WeatherResponse> getWeatherForecastData(String cityName) {
        requestNewForeCastData(cityName);
        return mData;
    }

    private void requestNewForeCastData(String cityName) {
        new loadWeatherAsyncTask(mWeatherAPI, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    mData.setValue(response.body());
                    Log.d("Async Rest Api success", "Code: " + response.code());
                } else {
                    Log.d("Async Rest Api failed", "Code: " + response.code() + ": " + response.raw().message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.d("Resp Error", t.getMessage());
            }
        }).execute(cityName);
    }

    private static class loadWeatherAsyncTask extends AsyncTask<String, Void, Void> {

        private final String APP_ID = "45a87f9aadf5fddecd27d0d1a5da8ba8";
        private WeatherAPI mWeatherAPI;
        private Callback<WeatherResponse> mCallBack;

        loadWeatherAsyncTask(WeatherAPI weatherAPI, Callback<WeatherResponse> callBack) {
            this.mWeatherAPI = weatherAPI;
            this.mCallBack = callBack;
        }

        @Override
        protected Void doInBackground(String... params) {
            mWeatherAPI.getWeatherForecastByCityName(params[0], APP_ID).enqueue(mCallBack);
            return null;
        }
    }
}
