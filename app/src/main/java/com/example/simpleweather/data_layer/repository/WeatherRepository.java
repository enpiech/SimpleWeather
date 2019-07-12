package com.example.simpleweather.data_layer.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.example.simpleweather.data_layer.data_source.WeatherForecastDatabase;
import com.example.simpleweather.data_layer.data_source.dao.CityDao;
import com.example.simpleweather.data_layer.data_source.dao.WeatherDetailsDao;
import com.example.simpleweather.data_layer.model.five_days_responses.City;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherResponse;
import com.example.simpleweather.data_layer.remote.RetrofitService;
import com.example.simpleweather.data_layer.remote.WeatherAPI;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    private static final String BASE_URL;
    private static final long FRESH_TIMEOUT;
    private static final String APP_ID;

    static {
        BASE_URL = "https://api.openweathermap.org/";
        APP_ID = "45a87f9aadf5fddecd27d0d1a5da8ba8";
        FRESH_TIMEOUT = (int) Math.ceil((System.currentTimeMillis() / 1000000) * 1000);
    }

    private final Executor mExecutor;
    private final WeatherAPI mWeatherAPI;
    private static CityDao mCityDao;
    private final WeatherDetailsDao mWeatherDetailsDao;
    private final MediatorLiveData<String> lastCity;

    private WeatherRepository(Application application) {
        mWeatherAPI = RetrofitService.createService(WeatherAPI.class, BASE_URL);

        WeatherForecastDatabase weatherDB = WeatherForecastDatabase.getDatabase(application);
        mCityDao = weatherDB.mCityDao();
        mWeatherDetailsDao = weatherDB.mWeatherDetailsDao();

        mExecutor = Executors.newSingleThreadExecutor();

        lastCity = new MediatorLiveData<>();

        getLastCity(mCityDao);
    }

    /**
     * Get city data from Room, auto-change if last city is changed
     * @return City nullable data
     */
    public LiveData<City> getCity() {
        return Transformations.switchMap(lastCity, mCityDao::getCity);
    }

    /**
     * Get weather response detail list from Room, auto-change if last city is changed
     * @return Nullable data list
     */
    public LiveData<List<WeatherDetail>> getList() {
        return Transformations.switchMap(lastCity, mWeatherDetailsDao::getWeatherDetails);
    }

    /**
     * Check forecast data of city is updated, if outdated, fetch data from rest api
     * @param cityName City name of needed forecast data
     */
    public void getForecastData(final String cityName) {
        mExecutor.execute(() -> {
            boolean isUpdated = mCityDao.hasCity(cityName, FRESH_TIMEOUT) > 0;
            if (isUpdated) {
                lastCity.postValue(cityName);
                return;
            }
            // FIXME Can be replace with WorkManager chaining work
            fetchForeCast(cityName);
        });
    }

    /**
     * Get last city from database
     */
    private void getLastCity(CityDao cityDao) {
        lastCity.addSource(cityDao.getLastCityName(), lastCity::setValue);
    }

    /**
     * Fetch forecast data from rest api then store in database if success response, update last_update column
     * @param cityName City name of needed forecast data
     */
    private void fetchForeCast(String cityName) {
        mWeatherAPI.getWeatherForecastByCityName(cityName, APP_ID).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    response.body().getCity().setLastUpdate(FRESH_TIMEOUT);

                    insert(response.body().getCity());
                    insertWeatherListFrom(response.body());
                } else {
                    // TODO Notify user that there something wrong when get data
                    Log.d("abc", response.message());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // TODO display error if failed
            }
        });
    }

    /**
     * Insert city data to Room database
     * @param city City data - take from rest api
     */
    private void insert(City city) {
        mExecutor.execute(() -> mCityDao.insertCity(city));
    }

    /**
     * Insert weather details list from weather response to Room database
     * @param weatherResponse Weather response data - take from rest api
     */
    private void insertWeatherListFrom(WeatherResponse weatherResponse) {
        mExecutor.execute(() -> {
            for (WeatherDetail param : weatherResponse.getListWeatherDetails()) {
                param.setCityName(weatherResponse.getCity().getName());
                mWeatherDetailsDao.insertWeatherDetails(param);
            }
            lastCity.postValue(weatherResponse.getCity().getName());
        });
    }
}
