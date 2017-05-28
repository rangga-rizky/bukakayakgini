package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponseProvinsi {
    @SerializedName("data")
    @Expose
    private ArrayList<Provinsi> data = new ArrayList<>();

    public void setData(ArrayList<Provinsi> data) {
        this.data = data;
    }

    public ArrayList<Provinsi> getData() {
        return data;
    }
}
