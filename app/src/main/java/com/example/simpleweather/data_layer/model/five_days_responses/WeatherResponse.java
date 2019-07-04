
package com.example.simpleweather.data_layer.model.five_days_responses;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.simpleweather.data_layer.constants.DBConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse extends BaseObservable {

    /**
     * cod : 2xx code for success, 4xx - 5xx code for failure
     * message : return double if success, return string if failed request
     * cnt : number of lines returned by API call
     * list :
     * city :
     */

    @Ignore
    @SerializedName("cod")
    @Expose
    private Integer cod;

    @Ignore
    @SerializedName("message")
    @Expose
    private String message;

    @Ignore
    @SerializedName("cnt")
    @Expose
    private Integer cnt;

    @SerializedName("list")
    @Expose
    private List<WeatherDetail> mListWeatherDetails = null;

    @SerializedName("city")
    @Expose
    private City city;

    @Bindable
    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Bindable
    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    @Bindable
    public List<WeatherDetail> getListWeatherDetails() {
        return mListWeatherDetails;
    }

    public void setListWeatherDetails(List<WeatherDetail> listWeatherDetails) {
        this.mListWeatherDetails = listWeatherDetails;
    }

    @Bindable
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
