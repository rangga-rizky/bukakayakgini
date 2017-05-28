package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponseCategory {
    @SerializedName("categories")
    @Expose
    private ArrayList<Kategori> kategoris = new ArrayList<>();

    public void setKategoris(ArrayList<Kategori> kategoris) {
        this.kategoris = kategoris;
    }

    public ArrayList<Kategori> getKategoris() {
        return kategoris;
    }
}
