package com.example.simpleweather.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.simpleweather.data.model.MainInfo;
import com.example.simpleweather.data.model.WeatherResponse;
import com.example.simpleweather.data.repository.WeatherRepository;

import java.util.List;


public class WeatherForecastViewModel extends ViewModel {
    private static String DEFAULT_CITY_NAME = "hanoi";

    private WeatherRepository mWeatherRepository;

    private MutableLiveData<WeatherResponse> mWeatherResponse = new MutableLiveData<>();
    private MutableLiveData<MainInfo> mSelection = new MutableLiveData<>();

    public WeatherForecastViewModel() {
        this.mWeatherRepository = WeatherRepository.getInstance();
        getWeatherResponse(DEFAULT_CITY_NAME);
    }

    public void getWeatherResponse(String cityName) {
        if (this.mWeatherResponse.getValue() == null) {
            this.mWeatherResponse = mWeatherRepository.getWeatherForecastData(cityName);
        } else {
            this.mWeatherResponse.setValue(mWeatherRepository.getWeatherForecastData(cityName).getValue());
        }
    }

    public LiveData<WeatherResponse> getResponseLiveData() {
        return mWeatherResponse;
    }

    public LiveData<List<MainInfo>> getAllList() {
        return Transformations.map(this.mWeatherResponse, WeatherResponse::getMainInfo);
    }

    public LiveData<MainInfo> getSelected() {
        if (this.mSelection.getValue() == null) {
            mWeatherResponse.observeForever(response -> mSelection.setValue(response.getMainInfo().get(0)));
        }
        return mSelection;
    }

    public void select(int pos) {
        if (this.mWeatherResponse.getValue() != null) {
            mSelection.setValue(this.mWeatherResponse.getValue().getMainInfo().get(pos));
        }
    }
}
