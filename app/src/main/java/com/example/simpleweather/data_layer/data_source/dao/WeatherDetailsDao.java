package com.example.simpleweather.data_layer.data_source.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpleweather.data_layer.constants.DBConstants;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;

@Dao
public interface WeatherDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertWeatherDetails(WeatherDetail...weatherDetails);

    @Update
    public void updateWeatherDetails(WeatherDetail...weatherDetails);

    @Query("SELECT * FROM " + DBConstants.WEATHER_TABLE_NAME)
    public LiveData<PagedList<WeatherDetail>> getWeatherDetailes();
}
