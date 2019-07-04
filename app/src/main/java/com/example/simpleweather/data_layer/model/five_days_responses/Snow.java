
package com.example.simpleweather.data_layer.model.five_days_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snow {

    @SerializedName("3h")
    @Expose
    private Double snowVolumeAt3h;

    public Snow() {
        snowVolumeAt3h = 0d;
    }

    public Double get3h() {
        return snowVolumeAt3h;
    }

    public void set3h(Double _3h) {
        this.snowVolumeAt3h = _3h;
    }

}
