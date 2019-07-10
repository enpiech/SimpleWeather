package com.example.simpleweather.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.simpleweather.data.model.five_days_responses.City;
import com.example.simpleweather.data.model.five_days_responses.WeatherDetail;
import com.example.simpleweather.data.model.five_days_responses.WeatherResponse;
import com.example.simpleweather.data.source.local.dao.CityDao;
import com.example.simpleweather.data.source.local.dao.WeatherDetailsDao;
import com.example.simpleweather.data.source.local.database.CityDatabase;
import com.example.simpleweather.data.source.local.database.WeatherDetailDatabase;
import com.example.simpleweather.data.source.remote.RetrofitService;
import com.example.simpleweather.data.source.remote.WeatherAPI;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String APP_ID = "45a87f9aadf5fddecd27d0d1a5da8ba8";

    private final WeatherAPI mWeatherAPI;

    private final CityDao mCityDao;
    private final WeatherDetailsDao mWeatherDetailsDao;
    private static MutableLiveData<WeatherResponse> mData = new MutableLiveData<>();

    private final static long FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1);

    private WeatherRepository(Application application) {
        this.mWeatherAPI = RetrofitService.createService(WeatherAPI.class, BASE_URL);

        CityDatabase cityDB = CityDatabase.getDatabase(application);
        WeatherDetailDatabase weatherDB = WeatherDetailDatabase.getDatabase(application);

        mCityDao = cityDB.mCityDao();
        mWeatherDetailsDao = weatherDB.mWeatherDetailsDao();
    }

    /**
     * Get weather response for given city name, if given city name doesn't have data in database, fetch from RestAPI
     * @param cityName city need to get weather response
     * @return weather response data
     */

    public LiveData<City> getLocalCity(String cityName) {
        refreshData(cityName);

        return mCityDao.getCity();
    }

    public LiveData<List<WeatherDetail>> getListResponse(String cityName) {
        refreshData(cityName);

        return mWeatherDetailsDao.getLastWeatherForecast(cityName);
    }

    private void refreshData(String cityName) {
        new loadWeatherAsyncTask(mWeatherAPI, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("abc", response.message());
                    response.body().getCity().setDate(FRESH_TIMEOUT);
                    insert(response.body().getCity());
                    insertWeatherListFrom(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                // TODO display error if failed
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

    private void insert(City city) {
        new insertCityAsyncTask(mCityDao).execute(city);
    }

    private static class insertCityAsyncTask extends AsyncTask<City, Void, Void> {

        private CityDao mAsyncTaskDao;

        insertCityAsyncTask(CityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final City... params) {
            mAsyncTaskDao.insertCity(params[0]);
            return null;
        }
    }

    // FIXME using work manager instead
    private void insertWeatherListFrom(WeatherResponse weatherResponse) {
        new insertWeatherAsyncTask(mWeatherDetailsDao).execute(weatherResponse);
    }

    private static class insertWeatherAsyncTask extends AsyncTask<WeatherResponse, Void, Void> {
        private WeatherDetailsDao mDao;

        insertWeatherAsyncTask(WeatherDetailsDao dao) { mDao = dao; }

        @Override
        protected Void doInBackground(WeatherResponse... weatherResponses) {
            for (WeatherDetail param : weatherResponses[0].getListWeatherDetails()) {
                param.setCityName(weatherResponses[0].getCity().getName());

                mDao.insertWeatherDetails(param);
            }
            return null;
        }
    }
}
