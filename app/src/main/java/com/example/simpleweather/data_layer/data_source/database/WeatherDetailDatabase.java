package com.example.simpleweather.data_layer.data_source.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.simpleweather.data_layer.constants.DBConstants;
import com.example.simpleweather.data_layer.data_source.dao.WeatherDetailsDao;
import com.example.simpleweather.data_layer.model.five_days_responses.WeatherDetail;

@Database(entities = WeatherDetail.class, version = 1, exportSchema = false)
public abstract class WeatherDetailDatabase extends RoomDatabase {
    public abstract WeatherDetailsDao mWeatherDetailsDao();
    private static volatile WeatherDetailDatabase INSTANCE;

    public static WeatherDetailDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherDetailDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WeatherDetailDatabase.class, DBConstants.WEATHER_TABLE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
