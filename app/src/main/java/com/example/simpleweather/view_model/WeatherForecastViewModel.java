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
    private final LiveData<List<WeatherDetail>> mAllInfo = Transformations.map(this.mWeatherResponse, WeatherResponse::getListWeatherDetails);
    private final LiveData<String> mCity = Transformations.map(this.mWeatherResponse, result -> result.getCity().getName() + ", " + result.getCity().getCountry());
    private final LiveData<String> mWeatherDescription = Transformations.map(this.mSelection, result -> result.getConditionCode().get(0).getDescription());
    private final LiveData<String> mIconName = Transformations.map(this.mSelection, result -> result.getConditionCode().get(0).getIcon());
    private final LiveData<Double> mTempMin = Transformations.map(this.mSelection, result -> result.getMainWeatherInfo().getTempMin());
    private final LiveData<Double> mTempMax = Transformations.map(this.mSelection, result -> result.getMainWeatherInfo().getTempMax());
    private final LiveData<Double> mRainVolume = Transformations.map(this.mSelection, result -> result.getRain().getRainVolumeAt3h());
    private final LiveData<Double> mWindSpeed = Transformations.map(this.mSelection, result -> result.getWind().getSpeed());
    private final LiveData<Double> mCloudsPercent = Transformations.map(this.mSelection, result -> result.getClouds().getAll());
    private final LiveData<Double> mSnowVolume = Transformations.map(this.mSelection, result -> result.getSnow().getSnowVolumeAt3h());
    private final LiveData<Double> mHumidityPercent = Transformations.map(this.mSelection, result -> result.getMainWeatherInfo().getHumidity());
    private final LiveData<Double> mPressure = Transformations.map(this.mSelection, result -> result.getMainWeatherInfo().getPressure());

    public WeatherForecastViewModel(Application application) {
        super(application);
        this.mWeatherRepository = WeatherRepository.getInstance(application);
        // Init data
        mSelectedPos.setValue(0);
        mCurrentCity.setValue(DEFAULT_CITY_NAME);
        mItemListener.setValue(this::selectInfoAt);
    }

    public void requestForecastData(String cityName) {
        this.mCurrentCity.setValue(cityName);
    }

    public void selectInfoAt(int pos) {
        this.mSelectedPos.setValue(pos);
    }

    public LiveData<List<WeatherDetail>> getAllList() {
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
