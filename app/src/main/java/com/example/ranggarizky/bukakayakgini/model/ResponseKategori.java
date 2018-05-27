package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponseKategori {
    @SerializedName("data")
    @Expose
    private ArrayList<Kategori> data = new ArrayList<>();


    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("total")
    @Expose
    private String total;

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setData(ArrayList<Kategori> data) {
        this.data = data;
    }

    public ArrayList<Kategori> getData() {
        return data;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
