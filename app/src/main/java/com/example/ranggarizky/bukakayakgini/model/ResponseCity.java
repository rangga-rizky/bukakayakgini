package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponseCity {
    @SerializedName("data")
    @Expose
    private ArrayList<Kota> data = new ArrayList<>();

    public void setData(ArrayList<Kota> data) {
        this.data = data;
    }

    public ArrayList<Kota> getData() {
        return data;
    }
}
