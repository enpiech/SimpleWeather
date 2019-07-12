package com.example.simpleweather.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.simpleweather.data_layer.model.five_days_responses.City;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;
import com.example.simpleweather.data_layer.repository.WeatherRepository;
import com.example.simpleweather.listener.WeatherItemListener;

import java.util.List;


public class WeatherForecastViewModel extends AndroidViewModel {
    private final WeatherRepository mWeatherRepository;

    private MutableLiveData<Integer> mSelectedPos = new MutableLiveData<>();

    public final LiveData<WeatherDetail> mSelection = Transformations.switchMap(mSelectedPos, this::getMainInfo);
    private final LiveData<List<WeatherDetail>> data;
    private final LiveData<City> mCity;

    public WeatherForecastViewModel(Application application) {
        super(application);
        mWeatherRepository = WeatherRepository.getInstance(application);

        data = this.mWeatherRepository.getList();
        mCity = this.mWeatherRepository.getCity();

        // Init data
        mSelectedPos.setValue(0);
        mItemListener.setValue(this::selectInfoAt);
    }

    public void requestForecastData(String cityName) {
        this.mWeatherRepository.getForecastData(cityName);
    }

    public LiveData<List<WeatherDetail>> getAllList() {
        return data;
    }

    public LiveData<String> getCity() {
        return Transformations.map(this.mCity, result -> result != null ? result.getName() + ", " + result.getCountry() : null);
    }

    public LiveData<String> getWeatherDescription() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getConditionCode().get(0).getDescription() : null);
    }

    public LiveData<String> getIcon() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getConditionCode().get(0).getIcon() : null);
    }

    public LiveData<Double> getTempMin() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getMainWeatherInfo().getTempMin() : null);
    }

    public LiveData<Double> getTempMax() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getMainWeatherInfo().getTempMax() : null);
    }

    public LiveData<Double> getRainVolume() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getRain().getRainVolumeAt3h() : null);
    }

    public LiveData<Double> getWindSpeed() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getWind().getSpeed() : null);
    }

    public LiveData<Double> getCloudsPercent() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getClouds().getAll() : null);
    }

    public LiveData<Double> getSnowVolume() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getSnow().getSnowVolumeAt3h() : null);
    }

    public LiveData<Double> getHumidityPercent() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getMainWeatherInfo().getHumidity() : null);
    }

    public LiveData<Double> getPressure() {
        return Transformations.map(this.mSelection, result -> result != null ? result.getMainWeatherInfo().getPressure() : null);
    }

    public LiveData<WeatherItemListener> getItemListener() {
        return mItemListener;
    }

    private LiveData<WeatherDetail> getMainInfo(int pos) {
        return Transformations.map(this.data, result -> result.size() != 0 ? result.get(pos) : null);
    }

    private void selectInfoAt(int pos) {
        this.mSelectedPos.setValue(pos);
    }

    private MutableLiveData<WeatherItemListener> mItemListener = new MutableLiveData<>();


}
