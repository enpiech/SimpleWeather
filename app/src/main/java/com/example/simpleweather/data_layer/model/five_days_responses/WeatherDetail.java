
package com.example.simpleweather.data_layer.model.five_days_responses;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.simpleweather.data_layer.constants.DBConstants;
import com.example.simpleweather.data_layer.data_source.Converter.WeatherConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = DBConstants.WEATHER_TABLE_NAME)
public class WeatherDetail extends BaseObservable {

    @PrimaryKey
    @ColumnInfo(name = DBConstants.CITY_ID)
    @NonNull
    private Integer cityId;

    @ColumnInfo(name = DBConstants.WEATHER_DATE)
    @SerializedName("dt")
    @Expose
    private Integer dt;

    @Embedded
    @SerializedName("main")
    @Expose
    private MainWeatherInfo mMainWeatherInfo;

    @TypeConverters(WeatherConverter.class)
    @SerializedName("weather")
    @Expose
    private List<ConditionCode> mConditionCode = null;

    @Embedded
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @Embedded
    @SerializedName("wind")
    @Expose
    private Wind wind;

    @Embedded
    @SerializedName("sys")
    @Expose
    private Sys sys;

    @Embedded
    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;

    @Embedded
    @SerializedName("rain")
    @Expose
    private Rain rain;

    @Embedded
    @SerializedName("snow")
    @Expose
    private Snow snow;

    public WeatherDetail() {}

    @NonNull
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(@NonNull Integer cityId) {
        this.cityId = cityId;
    }

    @Bindable
    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    @Bindable
    public MainWeatherInfo getMainWeatherInfo() {
        return mMainWeatherInfo;
    }

    public void setMainWeatherInfo(MainWeatherInfo mainWeatherInfo) {
        this.mMainWeatherInfo = mainWeatherInfo;
    }

    @Bindable
    public List<ConditionCode> getConditionCode() {
        return mConditionCode;
    }

//    @Bindable
//    public ConditionCode getConditionCode() {
//        return mConditionCode.get(0);
//    }

    public void setConditionCode(List<ConditionCode> conditionCode) {
        this.mConditionCode = conditionCode;
    }

    @Bindable
    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    @Bindable
    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @Bindable
    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    @Bindable
    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

    @Bindable
    public Rain getRain() {
        if (rain == null) {
            rain = new Rain();
        }
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    @Bindable
    public Snow getSnow() {
        if (snow == null) {
            snow = new Snow();
        }
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public static final DiffUtil.ItemCallback<WeatherDetail> DIFF_CALLBACK = new DiffUtil.ItemCallback<WeatherDetail>() {
        @Override
        public boolean areItemsTheSame(@NonNull WeatherDetail oldItem, @NonNull WeatherDetail newItem) {
            return oldItem.mMainWeatherInfo.equals(newItem.mMainWeatherInfo);
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeatherDetail oldItem, @NonNull WeatherDetail newItem) {
            return oldItem.dt.equals(newItem.dt);
        }
    };

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
