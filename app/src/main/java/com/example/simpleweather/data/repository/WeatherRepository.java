package com.example.simpleweather.data.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.simpleweather.data.model.MainInfo;
import com.example.simpleweather.data.model.WeatherResponse;
import com.example.simpleweather.data.remote.RetrofitService;
import com.example.simpleweather.data.remote.WeatherAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    // Singleton
    private static WeatherRepository sWeatherRepository;

    public static WeatherRepository getInstance() {
        if (sWeatherRepository == null) {
            sWeatherRepository = new WeatherRepository();
        }
        return sWeatherRepository;
    }

    private static String APP_ID = "45a87f9aadf5fddecd27d0d1a5da8ba8";
    private static String BASE_URL = "https://api.openweathermap.org/";
    private WeatherAPI mWeatherAPI;
    private static MutableLiveData<WeatherResponse> mData = new MutableLiveData<>();

    public WeatherRepository() {
        this.mWeatherAPI = RetrofitService.createService(WeatherAPI.class, BASE_URL);
    }

    public MutableLiveData<WeatherResponse> getWeatherForecastData(String cityName) {
        new loadWeatherAsyncTask(mWeatherAPI).execute(cityName);
        return mData;
    }

    private static class loadWeatherAsyncTask extends AsyncTask<String, Void, Void> {

        private WeatherAPI mWeatherAPI;

        loadWeatherAsyncTask(WeatherAPI weatherAPI) {
            this.mWeatherAPI = weatherAPI;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mWeatherAPI.getWeatherForecastByCityName(strings[0], APP_ID).enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful()) {

                        mData.setValue(response.body());
                        Log.d("abc", mData.getValue().getCity().getName());
                    } else {
                        Log.d("abc", response.code() + "");
                        Log.d("abcfail", strings[0]);
                    }

                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Log.d("Resp Error", t.getMessage());
                }
            });
            return null;
        }
    }
}
