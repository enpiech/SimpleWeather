package com.example.simpleweather.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherResponse;
import com.example.simpleweather.data_layer.repository.WeatherRepository;
import com.example.simpleweather.listener.WeatherItemListener;

import java.util.List;


public class WeatherForecastViewModel extends AndroidViewModel {
    // TODO Get current location of user
    private final String DEFAULT_CITY_NAME = "hanoi";

    private final WeatherRepository mWeatherRepository;

    private MutableLiveData<Integer> mSelectedPos = new MutableLiveData<>();
    private MutableLiveData<String> mCurrentCity = new MutableLiveData<>();

    private final LiveData<WeatherResponse> mWeatherResponse = Transformations.switchMap(this.mCurrentCity, this::getWeatherResponse);
    private final LiveData<WeatherDetail> mSelection = Transformations.switchMap(this.mSelectedPos, this::getMainInfo);

    public WeatherForecastViewModel(Application application) {
        super(application);
        this.mWeatherRepository = WeatherRepository.getInstance(application);
        // Init data
        mSelectedPos.setValue(0);
        mCurrentCity.setValue("");
        mItemListener.setValue(this::selectInfoAt);
    }

    public void requestForecastData(String cityName) {
        this.mCurrentCity.setValue(cityName);
    }

    public void selectInfoAt(int pos) {
        this.mSelectedPos.setValue(pos);
    }

    public LiveData<List<WeatherDetail>> getAllList() {
        return Transformations.map(this.mWeatherResponse, WeatherResponse::getListWeatherDetails);
    }

    public LiveData<String> getCity() {
        return Transformations.map(this.mWeatherResponse, result -> result.getCity().getName() + ", " + result.getCity().getCountry());
    }

    public LiveData<String> getWeatherDescription() {
        return Transformations.map(this.mSelection, result -> result.getConditionCode().get(0).getDescription());
    }

    public LiveData<String> getIcon() {
        return Transformations.map(this.mSelection, result -> result.getConditionCode().get(0).getIcon());
    }

    public LiveData<Double> getTempMin() {
        return Transformations.map(this.mSelection, result -> result.getMainWeatherInfo().getTempMin());
    }

    public LiveData<Double> getTempMax() {
        return Transformations.map(this.mSelection, result -> result.getMainWeatherInfo().getTempMax());
    }

    public LiveData<Double> getRainVolume() {
        return Transformations.map(this.mSelection, result -> result.getRain().getRainVolumeAt3h());
    }

    public LiveData<Double> getWindSpeed() {
        return Transformations.map(this.mSelection, result -> result.getWind().getSpeed());
    }

    public LiveData<Double> getCloudsPercent() {
        return Transformations.map(this.mSelection, result -> result.getClouds().getAll());
    }

    public LiveData<Double> getSnowVolume() {
        return Transformations.map(this.mSelection, result -> result.getSnow().getSnowVolumeAt3h());
    }

    public LiveData<Double> getHumidityPercent() {
        return Transformations.map(this.mSelection, result -> result.getMainWeatherInfo().getHumidity());
    }

    public LiveData<Double> getPressure() {
        return Transformations.map(this.mSelection, result -> result.getMainWeatherInfo().getPressure());
    }

    private LiveData<WeatherDetail> getMainInfo(int pos) {
        return Transformations.map(this.mWeatherResponse, result -> result.getListWeatherDetails().get(pos));
    }

    private LiveData<WeatherResponse> getWeatherResponse(String cityName) {
        return this.mWeatherRepository.getWeatherResponse(cityName);
    }

    private MutableLiveData<WeatherItemListener> mItemListener = new MutableLiveData<>();

    public LiveData<WeatherItemListener> getItemListener() {
        return mItemListener;
    }
}
