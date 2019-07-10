package com.example.simpleweather.data.source.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.simpleweather.data.constants.DBConstants;
import com.example.simpleweather.data.model.five_days_responses.City;

@Dao
public interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(City city);

    @Query("SELECT * FROM " + DBConstants.CITY_TABLE_NAME)
    LiveData<City> getCity();

    @Query("SELECT COUNT(*) FROM " + DBConstants.CITY_TABLE_NAME + " WHERE last_update >= :timeout")
    int hasCity(long timeout);

//    @Query("SELECT * FROM " + DBConstants.CITY_TABLE_NAME + " WHERE " + DBConstants.CITY_COUNTRY + " LIKE :countryCode")
//    public LiveData<List<City>> getCitiesIn(String countryCode);

    @Query("DELETE FROM " + DBConstants.CITY_TABLE_NAME)
    void deleteAll();
}
