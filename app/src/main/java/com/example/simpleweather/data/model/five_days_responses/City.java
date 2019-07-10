
package com.example.simpleweather.data.model.five_days_responses;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.simpleweather.data.constants.DBConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
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

    @SerializedName("country")
    @Expose
    private String country;

    @ColumnInfo(name = "last_update")
    private Long mDate;

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

    public Long getDate() {
        return mDate;
    }

    public void setDate(Long date) {
        mDate = date;
    }
}
