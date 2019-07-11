package com.example.simpleweather.data_layer.data_source.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpleweather.data_layer.constants.DBConstants;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;

import java.util.List;

@Dao
public interface WeatherDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeatherDetails(WeatherDetail...weatherDetails);

    @Update
    void updateWeatherDetails(WeatherDetail...weatherDetails);

    @Query("DELETE FROM " + DBConstants.WEATHER_TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + DBConstants.WEATHER_TABLE_NAME)
    List<WeatherDetail> getWeatherDetails();

    @Query("SELECT * FROM " + DBConstants.WEATHER_TABLE_NAME + " WHERE " + DBConstants.CITY_NAME + " LIKE :cityName")
    LiveData<List<WeatherDetail>> getWeatherDetails(String cityName);

    @Query("SELECT * FROM " + DBConstants.WEATHER_TABLE_NAME + " WHERE " + DBConstants.CITY_ID + " LIKE :cityId")
    LiveData<List<WeatherDetail>> getWeatherDetails(Integer cityId);

    @Query("SELECT * FROM " + DBConstants.WEATHER_TABLE_NAME + " ORDER BY " + DBConstants.WEATHER_DATE +  " DESC LIMIT 1")
    LiveData<List<WeatherDetail>> getLastWeatherForecast();
}
