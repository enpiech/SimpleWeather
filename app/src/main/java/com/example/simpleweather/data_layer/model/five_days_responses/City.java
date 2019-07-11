
package com.example.simpleweather.data_layer.model.five_days_responses;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.simpleweather.data_layer.constants.DBConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = DBConstants.CITY_TABLE_NAME)
public class City extends BaseObservable {

    @PrimaryKey
    @ColumnInfo(name = DBConstants.CITY_ID)
    @SerializedName("id")
    @Expose
    private Integer id;

    @ColumnInfo(name = DBConstants.CITY_NAME)
    @SerializedName("name")
    @Expose
    private String name;

    @Embedded
    @SerializedName("coord")
    @Expose
    private Coord coord;

    @ColumnInfo(name = DBConstants.CITY_COUNTRY)
    @SerializedName("country")
    @Expose
    private String country;

    @ColumnInfo(name = DBConstants.LAST_UPDATE)
    private long lastUpdate;

    @Bindable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    @Bindable
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
