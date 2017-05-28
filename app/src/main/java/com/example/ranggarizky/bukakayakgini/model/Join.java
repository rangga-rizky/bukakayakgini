package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ranggarizky on 1/19/2017.
 */
public class Join {

    @SerializedName("secret")
    @Expose
    private String secret;

    @SerializedName("id_join")
    @Expose
    private String id_join;

    @SerializedName("id_buyer")
    @Expose
    private String id_buyer;

    @SerializedName("id_demand")
    @Expose
    private String id_demand;

    @SerializedName("pesan")
    @Expose
    private String pesan;

    public void setId_buyer(String id_buyer) {
        this.id_buyer = id_buyer;
    }

    public void setId_demand(String id_demand) {
        this.id_demand = id_demand;
    }

    public String getId_buyer() {
        return id_buyer;
    }

    public String getId_demand() {
        return id_demand;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPesan() {
        return pesan;
    }

    public String getSecret() {
        return secret;
    }

    public void setId_join(String id_join) {
        this.id_join = id_join;
    }

    public String getId_join() {
        return id_join;
    }
}
