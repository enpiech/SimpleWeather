package com.example.simpleweather.helper;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.Locale;

public class BindingUtils {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imgUrl) {
        Glide.with(view.getContext())
                .load("http://openweathermap.org/img/wn/" + imgUrl + "@2x.png")
                .into(view);
    }

    @BindingAdapter({"temperatureDegree"})
    public static void setTemperatureText(TextView view, Double degree) {
        if (degree == null) {
            degree = -1d;
        }
        String text = String.format(Locale.getDefault(), "%d \u2103", Math.round(degree));
        view.setText(text);
    }

    @BindingAdapter({"dateOfWeekIntValue"})
    public static void setDateOfWeekTxt(TextView view, int date) {
        Date calendar = new Date(date * 1000L);
        String text = String.format("%1$tA", calendar);
        view.setText(text);
    }

    @BindingAdapter({"timeOfDateIntValue"})
    public static void setTimeOfDateTxt(TextView view, int date) {
        Date calendar = new Date(date * 1000L);
        String text = String.format("%tR", calendar);
        view.setText(text);
    }

    @BindingAdapter({"volumeValue"})
    public static void setVolume(TextView view, double volume) {
        view.setText(String.format("%s m²", volume));
    }

    @BindingAdapter({"windSpeedValue"})
    public static void setWindSpeed(TextView view, double speed) {
        view.setText(String.format("%s m/s", speed));
    }

    @BindingAdapter({"percentValue"})
    public static void setPercent(TextView view, double speed) {
        view.setText(String.format("%s %%", speed));
    }

    @BindingAdapter({"levelValue"})
    public static void setLevel(TextView view, double speed) {
        view.setText(String.format("%s hPa", speed));
    }
}
