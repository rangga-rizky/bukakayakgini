package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class Kategori_detail implements Serializable {
    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("id")
    @Expose
    private String id;

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
