package com.example.simpleweather.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.simpleweather.data_layer.model.MainInfo;
import com.example.simpleweather.data_layer.model.WeatherResponse;
import com.example.simpleweather.data_layer.repository.WeatherRepository;

import java.util.List;


public class WeatherForecastViewModel extends ViewModel {
    private final String DEFAULT_CITY_NAME = "hanoi";

    private final WeatherRepository mWeatherRepository;

    private MutableLiveData<Integer> mSelectedPos = new MutableLiveData<>();
    private MutableLiveData<String> mCurrentCity = new MutableLiveData<>();

    private final LiveData<WeatherResponse> mWeatherResponse = Transformations.switchMap(this.mCurrentCity, this::getWeatherResponse);
    private final LiveData<MainInfo> mSelection = Transformations.switchMap(this.mSelectedPos, this::getMainInfo);
    private final LiveData<List<MainInfo>> mAllInfo = Transformations.map(this.mWeatherResponse, WeatherResponse::getMainInfo);
    private final LiveData<String> mCity = Transformations.map(this.mWeatherResponse, result -> result.getCity().getName() + ", " + result.getCity().getCountry());
    private final LiveData<String> mWeatherDescription = Transformations.map(this.mSelection, result -> result.getWeather().getDescription());
    private final LiveData<String> mIconName = Transformations.map(this.mSelection, result -> result.getWeather().getIcon());
    private final LiveData<Double> mTempMin = Transformations.map(this.mSelection, result -> result.getExtendedInfo().getTempMin());
    private final LiveData<Double> mTempMax = Transformations.map(this.mSelection, result -> result.getExtendedInfo().getTempMax());
    private final LiveData<Double> mRainVolume = Transformations.map(this.mSelection, result -> result.getRain() != null ? result.getRain().get3h() : 0d);
    private final LiveData<Double> mWindSpeed = Transformations.map(this.mSelection, result -> result.getWind().getSpeed());
    private final LiveData<Double> mCloudsPercent = Transformations.map(this.mSelection, result -> result.getClouds().getAll());
    private final LiveData<Double> mSnowVolume = Transformations.map(this.mSelection, result -> result.getSnow() != null ? result.getSnow().get3h() : 0d);
    private final LiveData<Double> mHumidityPercent = Transformations.map(this.mSelection, result -> result.getExtendedInfo().getHumidity());
    private final LiveData<Double> mPressure = Transformations.map(this.mSelection, result -> result.getExtendedInfo().getPressure());

    public WeatherForecastViewModel() {
        this.mWeatherRepository = WeatherRepository.getInstance();
        // Init data
        mSelectedPos.setValue(0);
        mCurrentCity.setValue(DEFAULT_CITY_NAME);
    }

    public void requestForecastData(String cityName) {
        this.mCurrentCity.setValue(cityName);
    }

    public void selectInfoAt(int pos) {
        this.mSelectedPos.setValue(pos);
    }

    public LiveData<List<MainInfo>> getAllList() {
        return mAllInfo;
    }

    public LiveData<String> getCity() {
        return mCity;
    }

    public LiveData<String> getWeatherDescription() {
        return mWeatherDescription;
    }

    public LiveData<String> getIcon() {
        return mIconName;
    }

    public LiveData<Double> getTempMin() {
        return mTempMin;
    }

    public LiveData<Double> getTempMax() {
        return mTempMax;
    }

    public LiveData<Double> getRainVolume() {
        return mRainVolume;
    }

    public LiveData<Double> getWindSpeed() {
        return mWindSpeed;
    }

    public LiveData<Double> getCloudsPercent() {
        return mCloudsPercent;
    }

    public LiveData<Double> getSnowVolume() {
        return mSnowVolume;
    }

    public LiveData<Double> getHumidityPercent() {
        return mHumidityPercent;
    }

    public LiveData<Double> getPressure() {
        return mPressure;
    }

    private LiveData<MainInfo> getMainInfo(int pos) {
        return Transformations.map(this.mWeatherResponse, result -> result.getMainInfo().get(pos));
    }

    private LiveData<WeatherResponse> getWeatherResponse(String cityName) {
        return this.mWeatherRepository.getWeatherResponse(cityName);
    }
}
