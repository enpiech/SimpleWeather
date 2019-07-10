
package com.example.simpleweather.data.model.five_days_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snow {

    @SerializedName("3h")
    @Expose
    private Double snowVolumeAt3h;

    public Snow() {
        snowVolumeAt3h = 0d;
    }

    public Double getSnowVolumeAt3h() {
        return snowVolumeAt3h;
    }

    public void setSnowVolumeAt3h(Double _3h) {
        this.snowVolumeAt3h = _3h;
    }

}
