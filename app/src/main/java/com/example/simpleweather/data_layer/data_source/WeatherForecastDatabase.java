package com.example.simpleweather.data_layer.data_source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.simpleweather.data_layer.constants.DBConstants;
import com.example.simpleweather.data_layer.data_source.dao.CityDao;
import com.example.simpleweather.data_layer.data_source.dao.WeatherDetailsDao;
import com.example.simpleweather.data_layer.model.five_days_responses.City;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;

@Database(entities = {WeatherDetail.class, City.class}, version = 1, exportSchema = false)
public abstract class WeatherForecastDatabase extends RoomDatabase {
    public abstract WeatherDetailsDao mWeatherDetailsDao();
    public abstract CityDao mCityDao();
    private static volatile WeatherForecastDatabase INSTANCE;

    public static WeatherForecastDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherForecastDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WeatherForecastDatabase.class, DBConstants.WEATHER_TABLE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
