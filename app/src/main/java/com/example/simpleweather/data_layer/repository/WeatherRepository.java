package com.example.simpleweather.data_layer.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.simpleweather.data_layer.data_source.dao.CityDao;
import com.example.simpleweather.data_layer.data_source.database.CityDatabase;
import com.example.simpleweather.data_layer.model.five_days_responses.City;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherResponse;
import com.example.simpleweather.data_layer.remote.RetrofitService;
import com.example.simpleweather.data_layer.remote.WeatherAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private static WeatherRepository sWeatherRepository;

    public static WeatherRepository getInstance(Application application) {
        if (sWeatherRepository == null) {
            sWeatherRepository = new WeatherRepository(application);
        }
        return sWeatherRepository;
    }

    private final String BASE_URL = "https://api.openweathermap.org/";
    private final WeatherAPI mWeatherAPI;
    private final CityDao mCityDao;
    private static final MutableLiveData<WeatherResponse> mData = new MutableLiveData<>();

    private WeatherRepository(Application application) {
        this.mWeatherAPI = RetrofitService.createService(WeatherAPI.class, BASE_URL);
        CityDatabase db = CityDatabase.getDatabase(application);
        mCityDao = db.mCityDao();

//        mData.observeForever(weatherResponse -> {
//            if (weatherResponse.getCity() != null) {
//                insert(weatherResponse.getCity());
//            }
//        });
//
//        mCityDao.getCities().observeForever(cities -> Log.d("abc", cities.size() + ": " + cities.get(0).getName()));
    }

    public MutableLiveData<WeatherResponse> getWeatherResponse(String cityName) {
        requestNewForecastData(cityName);
        return mData;
    }

    private void requestNewForecastData(String cityName) {
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
        private final WeatherAPI mWeatherAPI;
        private final Callback<WeatherResponse> mCallBack;

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

    public void insert(City city) {
        new insertAsyncTask(mCityDao).execute(city);
    }

    private static class insertAsyncTask extends AsyncTask<City, Void, Void> {

        private CityDao mAsyncTaskDao;

        insertAsyncTask(CityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final City... params) {
            mAsyncTaskDao.insertCities(params[0]);
            return null;
        }
    }
}
