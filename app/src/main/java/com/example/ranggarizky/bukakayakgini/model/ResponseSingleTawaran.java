package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponseSingleTawaran {
    @SerializedName("data")
    @Expose
    private Tawaran data;


    public void setData(Tawaran data) {
        this.data = data;
    }

    public Tawaran getData() {
        return data;
    }
}
