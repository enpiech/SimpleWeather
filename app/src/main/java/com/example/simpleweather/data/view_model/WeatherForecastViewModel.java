package com.example.simpleweather.data.view_model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.simpleweather.data.model.MainInfo;
import com.example.simpleweather.data.model.WeatherResponse;
import com.example.simpleweather.data.repository.WeatherRepository;

import java.util.List;


public class WeatherForecastViewModel extends ViewModel {
    private WeatherRepository mWeatherRepository;

    private MutableLiveData<WeatherResponse> mWeatherResponse;
    private MutableLiveData<MainInfo> mSelection = new MutableLiveData<>();

    public WeatherForecastViewModel() {
        this.mWeatherRepository = WeatherRepository.getInstance();
        getWeatherResponse("hanoi");
    }

    public void getWeatherResponse(String cityName) {
        this.mWeatherResponse = mWeatherRepository.getWeatherForecastData(cityName);
    }

    public LiveData<WeatherResponse> getResponseLiveData() {
        return mWeatherResponse;
    }

    public LiveData<List<MainInfo>> getAllList() {
        return Transformations.map(this.mWeatherResponse, value -> value.getMainInfo());
    }

    public LiveData<MainInfo> getSelected() {
        mWeatherResponse.observeForever(response -> mSelection.setValue(response.getMainInfo().get(0)));
        return mSelection;
    }

    public void select(int pos) {
        mSelection.setValue(this.mWeatherResponse.getValue().getMainInfo().get(pos));
    }
}
