package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class Rating {
    @SerializedName("average_rate")
    @Expose
    private Float average_rate;

    @SerializedName("user_count")
    @Expose
    private String user_count;

    public void setAverage_rate(Float average_rate) {
        this.average_rate = average_rate;
    }

    public void setUser_count(String user_count) {
        this.user_count = user_count;
    }

    public Float getAverage_rate() {
        return average_rate;
    }

    public String getUser_count() {
        return user_count;
    }
}
