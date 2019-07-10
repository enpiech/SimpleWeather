package com.example.simpleweather.data.source.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.simpleweather.data.constants.DBConstants;
import com.example.simpleweather.data.source.local.dao.CityDao;
import com.example.simpleweather.data.model.five_days_responses.City;

@Database(entities = City.class, version = 1, exportSchema = false)
public abstract class CityDatabase extends RoomDatabase {
    public abstract CityDao mCityDao();
    private static volatile CityDatabase INSTANCE;

    public static CityDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CityDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CityDatabase.class, DBConstants.CITY_TABLE_NAME).build();
                }
            }

        }
        return INSTANCE;
    }
}