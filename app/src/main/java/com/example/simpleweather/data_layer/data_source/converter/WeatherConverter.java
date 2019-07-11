package com.example.simpleweather.data_layer.data_source.converter;

import androidx.room.TypeConverter;

import com.example.simpleweather.data_layer.model.five_days_responses.ConditionCode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class WeatherConverter implements Serializable {
    @TypeConverter
    public List<ConditionCode> toConditionCodeList(String conditionCodeString) {
        if (conditionCodeString == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<ConditionCode>>() {}.getType();
        return gson.fromJson(conditionCodeString, type);
    }

    @TypeConverter
    public String fromConditionCodeList(List<ConditionCode> conditionCodes) {
        if (conditionCodes == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<ConditionCode>>() {}.getType();
        return gson.toJson(conditionCodes, type);
    }
}
