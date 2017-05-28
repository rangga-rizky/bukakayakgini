package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponseObject {
    @SerializedName("products")
    @Expose
    private ArrayList<BarangBukaLapak> products = new ArrayList<>();

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("product")
    @Expose
    private BarangBukaLapak product;

    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("message")
    @Expose
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setProduct(BarangBukaLapak product) {
        this.product = product;
    }

    public BarangBukaLapak getProduct() {
        return product;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setProducts(ArrayList<BarangBukaLapak> products) {
        this.products = products;
    }

    public ArrayList<BarangBukaLapak> getProducts() {
        return products;
    }


}
