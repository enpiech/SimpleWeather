
package com.example.simpleweather.data.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainInfo extends BaseObservable {

    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("main")
    @Expose
    private ExtendedInfo mExtendedInfo;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;
    @SerializedName("rain")
    @Expose
    private Rain rain;
    @SerializedName("snow")
    @Expose
    private Snow snow;

    @Bindable
    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    @Bindable
    public ExtendedInfo getExtendedInfo() {
        return mExtendedInfo;
    }

    public void setExtendedInfo(ExtendedInfo extendedInfo) {
        this.mExtendedInfo = extendedInfo;
    }

    @Bindable
    public java.util.List<Weather> getWeathers() {
        return weather;
    }

    @Bindable
    public Weather getWeather() { return weather.get(0); }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
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
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    @Bindable
    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public static DiffUtil.ItemCallback<MainInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<MainInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull MainInfo oldItem, @NonNull MainInfo newItem) {
            return oldItem.mExtendedInfo.equals(newItem.mExtendedInfo);
        }

        @Override
        public boolean areContentsTheSame(@NonNull MainInfo oldItem, @NonNull MainInfo newItem) {
            return oldItem.dt.equals(newItem.dt);
        }
    };

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
