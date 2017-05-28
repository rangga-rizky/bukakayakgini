package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponsePermintaan {
    @SerializedName("data")
    @Expose
    private ArrayList<RequestObject> data = new ArrayList<>();

    @SerializedName("total")
    @Expose
    private String total;

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setData(ArrayList<RequestObject> data) {
        this.data = data;
    }

    public ArrayList<RequestObject> getData() {
        return data;
    }
}
