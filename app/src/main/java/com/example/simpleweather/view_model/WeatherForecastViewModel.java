package com.example.simpleweather.view_model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.simpleweather.data_layer.model.five_days_responses.City;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherResponse;
import com.example.simpleweather.data_layer.repository.WeatherRepository;
import com.example.simpleweather.listener.WeatherItemListener;

import java.util.List;


public class WeatherForecastViewModel extends AndroidViewModel {
    private final WeatherRepository mWeatherRepository;

    private MutableLiveData<Integer> mSelectedPos = new MutableLiveData<>();
    private MutableLiveData<String> mCurrentCity = new MutableLiveData<>();

    private final LiveData<WeatherDetail> mSelection = Transformations.switchMap(mSelectedPos, this::getMainInfo);

    public WeatherForecastViewModel(Application application) {
        super(application);
        mWeatherRepository = WeatherRepository.getInstance(application);
        mWeatherRepository.populateData();

        data = this.mWeatherRepository.getList();
        mCity = this.mWeatherRepository.getCity();

        // Init data
        mSelectedPos.setValue(0);
        mItemListener.setValue(this::selectInfoAt);
    }

//    public void requestForecastData(String cityName) {
//        this.mCurrentCity.setValue(cityName);
//    }
    public void requestForecastData(String cityName) {
        this.mWeatherRepository.getForecastData(cityName);
    }

    public void selectInfoAt(int pos) {
        this.mSelectedPos.setValue(pos);
    }

//    LiveData<List<WeatherDetail>> data = Transformations.switchMap(this.mCurrentCity, this::getList);
    private LiveData<List<WeatherDetail>> data;

//    private LiveData<List<WeatherDetail>> getList(String cityName) {
//        return mWeatherRepository.getList(cityName);
//    }

    public LiveData<List<WeatherDetail>> getAllList() {
        return data;
    }

//    private LiveData<City> mCity = Transformations.switchMap(this.mCurrentCity, this::getCityData);
    private LiveData<City> mCity;

//    private LiveData<City> getCityData(String cityName) {
//        return mWeatherRepository.getCity(cityName);
//    }

    public LiveData<String> getCity() {
//        return Transformations.map(this.mWeatherResponse, result -> result.getCity().getName() + ", " + result.getCity().getCountry());
        return Transformations.map(this.mCity, result -> result != null ? result.getName() + ", " + result.getCountry() : "");
    }

    public LiveData<String> getWeatherDescription() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getConditionCode().get(0).getDescription() : "");
    }

    public LiveData<String> getIcon() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getConditionCode().get(0).getIcon() : "");
    }

    public LiveData<Double> getTempMin() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getMainWeatherInfo().getTempMin() : -1);
    }

    public LiveData<Double> getTempMax() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getMainWeatherInfo().getTempMax() : -1);
    }

    public LiveData<Double> getRainVolume() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getRain().getRainVolumeAt3h() : -1);
    }

    public LiveData<Double> getWindSpeed() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getWind().getSpeed() : -1);
    }

    public LiveData<Double> getCloudsPercent() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getClouds().getAll() : -1);
    }

    public LiveData<Double> getSnowVolume() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getSnow().getSnowVolumeAt3h() : -1);
    }

    public LiveData<Double> getHumidityPercent() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getMainWeatherInfo().getHumidity() : -1);
    }

    public LiveData<Double> getPressure() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getMainWeatherInfo().getPressure() : -1);
    }

    private LiveData<WeatherDetail> getMainInfo(int pos) {
        return Transformations.map(this.data, result -> result.size() != 0 ? result.get(pos) : null);
    }

    private MutableLiveData<WeatherItemListener> mItemListener = new MutableLiveData<>();

    public LiveData<WeatherItemListener> getItemListener() {
        return mItemListener;
    }
}
