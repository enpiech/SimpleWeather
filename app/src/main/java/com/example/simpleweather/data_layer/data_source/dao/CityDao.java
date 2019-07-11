package com.example.simpleweather.data_layer.data_source.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.simpleweather.data_layer.constants.DBConstants;
import com.example.simpleweather.data_layer.model.five_days_responses.City;

import java.util.List;

@Dao
public interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(City city);

    @Query("SELECT * FROM " + DBConstants.CITY_TABLE_NAME)
    City getCity();

    @Query("SELECT * FROM " + DBConstants.CITY_TABLE_NAME + " WHERE " + DBConstants.CITY_NAME + " LIKE :cityName")
    LiveData<City> getCity(String cityName);

    @Query("SELECT COUNT(*) FROM " + DBConstants.CITY_TABLE_NAME + " WHERE " + DBConstants.CITY_NAME + " LIKE :cityName AND " + DBConstants.LAST_UPDATE + " >= :timeOut")
    Integer hasCity(String cityName, long timeOut);

    @Query("SELECT " + DBConstants.CITY_ID +
            " FROM " + DBConstants.CITY_TABLE_NAME +
            " WHERE " + DBConstants.LAST_UPDATE + " = (SELECT MAX(" + DBConstants.LAST_UPDATE + ") FROM " + DBConstants.CITY_TABLE_NAME + ") " +
            "LIMIT 1")
    LiveData<Integer> getLastCityId();

    @Query("SELECT " + DBConstants.CITY_NAME +
            " FROM " + DBConstants.CITY_TABLE_NAME +
            " WHERE " + DBConstants.LAST_UPDATE + " = (SELECT MAX(" + DBConstants.LAST_UPDATE + ") FROM " + DBConstants.CITY_TABLE_NAME + ") " +
            "LIMIT 1")
    LiveData<String> getLastCityName();
}
