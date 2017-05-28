package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class Kategori implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("children")
    @Expose
    private ArrayList<Kategori> children = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(ArrayList<Kategori> children) {
        this.children = children;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Kategori> getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }
}
