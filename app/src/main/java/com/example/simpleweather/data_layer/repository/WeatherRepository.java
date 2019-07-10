package com.example.simpleweather.data_layer.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.simpleweather.data_layer.data_source.dao.CityDao;
import com.example.simpleweather.data_layer.data_source.dao.WeatherDetailsDao;
import com.example.simpleweather.data_layer.data_source.database.CityDatabase;
import com.example.simpleweather.data_layer.data_source.database.WeatherDetailDatabase;
import com.example.simpleweather.data_layer.model.five_days_responses.City;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherResponse;
import com.example.simpleweather.data_layer.remote.RetrofitService;
import com.example.simpleweather.data_layer.remote.WeatherAPI;
import com.example.simpleweather.listener.OnEventListener;

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
    private final WeatherDetailsDao mWeatherDetailsDao;
    private static final MutableLiveData<WeatherResponse> mData = new MutableLiveData<>();

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
    public MutableLiveData<WeatherResponse> getWeatherResponse(String cityName) {
        if (cityName.isEmpty()) {
            new loadForecastData(mCityDao, mWeatherDetailsDao, mData::setValue).execute();
        } else {
            new loadLastSearchCity(mCityDao, object -> {
                if (object != null && object.getName().equalsIgnoreCase(cityName)) {
                    new loadForecastData(mCityDao, mWeatherDetailsDao, mData::setValue).execute();
                } else {
                    requestNewForecastData(cityName);
                }
            }).execute();
        }
        return mData;
    }

    /**
     * Fetch weather forecast data from RestAPI
     * If fetch success, update database with new data
     * @param cityName city need to get weather response
     */
    private void requestNewForecastData(String cityName) {
        new loadWeatherAsyncTask(mWeatherAPI, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    mData.setValue(response.body());

                    insert(mData.getValue().getCity());
                    insertWeatherListFrom(mData.getValue());
                } else {
                    // TODO display error if response failed
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

    private static class loadLastSearchCity extends AsyncTask<Void, Void, City> {
        private CityDao mDao;
        private OnEventListener<City> mListener;

        loadLastSearchCity(CityDao dao, OnEventListener<City> listener) {
            this.mDao = dao;
            this.mListener = listener;
        }

        @Override
        protected City doInBackground(Void... voids) {
            return mDao.getCity();
        }

        @Override
        protected void onPostExecute(City city) {
            if (mListener != null) {
                this.mListener.onReturn(city);
            }

            super.onPostExecute(city);
        }
    }


    private static class loadForecastData extends AsyncTask<Void, Void, WeatherResponse> {
        private CityDao mCityDao;
        private WeatherDetailsDao mWeatherDao;
        private OnEventListener<WeatherResponse> mResponse;

        loadForecastData(CityDao cityDao, WeatherDetailsDao weatherDao, OnEventListener<WeatherResponse> response) {
            this.mCityDao = cityDao;
            this.mWeatherDao = weatherDao;
            this.mResponse = response;
        }

        @Override
        protected WeatherResponse doInBackground(Void... voids) {
            WeatherResponse weatherResponse = new WeatherResponse();
            City city = mCityDao.getCity();
            List<WeatherDetail>listWeatherDetails = mWeatherDao.getWeatherDetails();
            weatherResponse.setCity(city);
            weatherResponse.setListWeatherDetails(listWeatherDetails);

            return weatherResponse;
        }

        @Override
        protected void onPostExecute(WeatherResponse weatherResponse) {
            if (mResponse != null) {
                mResponse.onReturn(weatherResponse);
            }

            super.onPostExecute(weatherResponse);
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
            mAsyncTaskDao.deleteAll();
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
//            mDao.deleteAll();
            for (WeatherDetail param : weatherResponses[0].getListWeatherDetails()) {
                mDao.insertWeatherDetails(param);
            }
            return null;
        }
    }
}
