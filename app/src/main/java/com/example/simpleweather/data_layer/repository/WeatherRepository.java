package com.example.simpleweather.data_layer.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.math.MathUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.simpleweather.data_layer.data_source.WeatherForecastDatabase;
import com.example.simpleweather.data_layer.data_source.dao.CityDao;
import com.example.simpleweather.data_layer.data_source.dao.WeatherDetailsDao;
import com.example.simpleweather.data_layer.model.five_days_responses.City;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherResponse;
import com.example.simpleweather.data_layer.remote.RetrofitService;
import com.example.simpleweather.data_layer.remote.WeatherAPI;
import com.example.simpleweather.listener.OnEventListener;

import java.util.List;
import java.util.concurrent.Executor;

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
    private static final long FRESH_TIMEOUT = (int) Math.ceil((System.currentTimeMillis() / 1000000.0) * 1000);

    private final Executor mExecutor;
    private final WeatherAPI mWeatherAPI;
    private final CityDao mCityDao;
    private final WeatherDetailsDao mWeatherDetailsDao;
    private static final MutableLiveData<WeatherResponse> mData = new MutableLiveData<>();
    private MediatorLiveData<String> lastCity;

    private WeatherRepository(Application application) {
        this.mWeatherAPI = RetrofitService.createService(WeatherAPI.class, BASE_URL);
        WeatherForecastDatabase weatherDB = WeatherForecastDatabase.getDatabase(application);

        mCityDao = weatherDB.mCityDao();
        mWeatherDetailsDao = weatherDB.mWeatherDetailsDao();
        mExecutor = Runnable::run;

        lastCity = new MediatorLiveData<>();
    }

    public void populateData() {
        lastCity.addSource(mCityDao.getLastCityName(), name -> lastCity.setValue(name));
    }

    public void getForecastData(String cityName) {
        lastCity.removeSource(mCityDao.getLastCityName());
        lastCity.setValue(cityName);
        refreshCity(cityName);
    }

//    public LiveData<City> getCity(String cityName) {
//        refreshCity(cityName);
//        return mCityDao.getCity(cityName);
//    }

    public LiveData<City> getCity() {
        return Transformations.switchMap(lastCity, mCityDao::getCity);
    }

//    public LiveData<List<WeatherDetail>> getList(String cityName) {
//        return mWeatherDetailsDao.getWeatherDetails(cityName);
//    }

    public LiveData<List<WeatherDetail>> getList() {
        return Transformations.switchMap(lastCity, mWeatherDetailsDao::getWeatherDetails);
    }

    /**
     * Check forecast data of city is updated, if outdated, fetch data from rest api
     * @param cityName City name of needed forecast data
     */
    private void refreshCity(final String cityName) {
        mExecutor.execute(() -> new hasUpdatedDataTask(mCityDao, isUpdated -> {
            if (isUpdated) return;
            // FIXME Can be replace with WorkManager chaining work
            fetchForeCast(cityName);
        }).execute(cityName));
    }

    /**
     * Fetch forecast data from rest api then store in database if success response
     * @param cityName City name of needed forecast data
     */
    private void fetchForeCast(String cityName) {
        new loadWeatherAsyncTask(mWeatherAPI, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    response.body().getCity().setLastUpdate(FRESH_TIMEOUT);
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

    private static class hasUpdatedDataTask extends AsyncTask<String, Void, Boolean> {

        private CityDao cityDao;
        private OnEventListener<Boolean> callback;

        hasUpdatedDataTask(CityDao cityDao, OnEventListener<Boolean> callback) {
            this.cityDao = cityDao;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
//            Integer cityExists = cityDao.hasCity(params[0], FRESH_TIMEOUT);
            Integer cityExists = cityDao.hasCity(params[0], FRESH_TIMEOUT);
            if (cityExists == 0) {
                Log.d("abc", "need fetch");
            } else {
                Log.d("abc", "cache");
            }
            return cityExists != 0;
        }

        @Override
        protected void onPostExecute(Boolean isExist) {
            callback.onReturn(isExist);
            super.onPostExecute(isExist);
        }
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    // FIXME using work manager instead
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
