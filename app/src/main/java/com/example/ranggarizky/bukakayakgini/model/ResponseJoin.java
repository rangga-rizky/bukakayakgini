package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponseJoin {
    @SerializedName("data")
    @Expose
    private ArrayList<Join> data = new ArrayList<>();

    @SerializedName("total")
    @Expose
    private String total;

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setData(ArrayList<Join> data) {
        this.data = data;
    }

    public ArrayList<Join> getData() {
        return data;
    }
}
