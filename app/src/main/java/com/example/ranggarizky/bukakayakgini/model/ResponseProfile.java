package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponseProfile {
    @SerializedName("data")
    @Expose
    private User data;

    public void setData(User data) {
        this.data = data;
    }

    public User getData() {
        return data;
    }
}
