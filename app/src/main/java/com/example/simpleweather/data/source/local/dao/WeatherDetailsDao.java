package com.example.simpleweather.data.source.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpleweather.data.constants.DBConstants;
import com.example.simpleweather.data.model.five_days_responses.WeatherDetail;

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

//    @Query("SELECT * FROM " + DBConstants.WEATHER_TABLE_NAME + " ORDER BY " + DBConstants.WEATHER_DATE +  " DESC")
//    LiveData<List<WeatherDetail>> getLastWeatherForecast();

    @Query("SELECT * FROM " + DBConstants.WEATHER_TABLE_NAME + " WHERE " + DBConstants.CITY_NAME + " LIKE :cityName")
    LiveData<List<WeatherDetail>> getLastWeatherForecast(String cityName);
}
