package com.example.simpleweather.data_layer.data_source.dao;

import androidx.lifecycle.LiveData;
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

//    @Query("SELECT * FROM " + DBConstants.CITY_TABLE_NAME + " WHERE " + DBConstants.CITY_COUNTRY + " LIKE :countryCode")
//    public LiveData<List<City>> getCitiesIn(String countryCode);

    @Query("DELETE FROM " + DBConstants.CITY_TABLE_NAME)
    void deleteAll();
}
