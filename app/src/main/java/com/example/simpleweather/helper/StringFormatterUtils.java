package com.example.simpleweather.helper;

import java.text.DateFormat;
import java.util.Date;

public class StringFormatterUtils {
    public static String getDateFormatted(int dateInt) {
        Date date = new Date(dateInt * 1000);
        return DateFormat.getDateInstance().format(date);
    }

    public static String getTemperatureText(Double degree) {
        return Math.round(degree) + "\\u2103";
    }
}
