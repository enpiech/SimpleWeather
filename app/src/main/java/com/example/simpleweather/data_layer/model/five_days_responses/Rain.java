
package com.example.simpleweather.data_layer.model.five_days_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("3h")
    @Expose
    private Double rainVolumeAt3h;

    public Rain() {
        this.rainVolumeAt3h = 0d;
    }

    public Double getRainVolumeAt3h() {
        return rainVolumeAt3h;
    }

    public void setRainVolumeAt3h(Double _3h) {
        this.rainVolumeAt3h = _3h;
    }

}
