
package com.example.simpleweather.data.model.five_days_responses;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConditionCode extends BaseObservable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;

    @Bindable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Bindable
    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Bindable
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static DiffUtil.ItemCallback<ConditionCode> DIFF_CALLBACK = new DiffUtil.ItemCallback<ConditionCode>() {
        @Override
        public boolean areItemsTheSame(@NonNull ConditionCode oldItem, @NonNull ConditionCode newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ConditionCode oldItem, @NonNull ConditionCode newItem) {
            return oldItem.id.equals(newItem.id);
        }
    };

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
