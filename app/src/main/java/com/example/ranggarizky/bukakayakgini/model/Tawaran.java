package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ranggarizky on 1/19/2017.
 */
public class Tawaran {

    @SerializedName("secret")
    @Expose
    private String secret;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_request")
    @Expose
    private String id_request;

    @SerializedName("id_seller")
    @Expose
    private String id_seller;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("id_produk")
    @Expose
    private String id_produk;

    @SerializedName("nama")
    @Expose
    private String nama;


    @SerializedName("demand")
    @Expose
    private RequestObject demand;



    @SerializedName("supplies")
    @Expose
    private ArrayList<BarangBukaLapak> details = new ArrayList<>();

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setDetails(ArrayList<BarangBukaLapak> details) {
        this.details = details;
    }

    public ArrayList<BarangBukaLapak> getDetails() {
        return details;
    }

    public void setDemand(RequestObject demand) {
        this.demand = demand;
    }

    public RequestObject getDemand() {
        return demand;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public void setId_request(String id_request) {
        this.id_request = id_request;
    }

    public void setId_seller(String id_seller) {
        this.id_seller = id_seller;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getId_produk() {
        return id_produk;
    }

    public String getId_request() {
        return id_request;
    }

    public String getId_seller() {
        return id_seller;
    }

    public String getSecret() {
        return secret;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
