package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ranggarizky on 1/19/2017.
 */
public class User {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("address")
    @Expose
    private Address address;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("avatar")
    @Expose
    private String avatar;


    @SerializedName("numDemand")
    @Expose
    private String numDemand;

    @SerializedName("numSupply")
    @Expose
    private String numSupply;

    @SerializedName("numGotDeal")
    @Expose
    private String numGotDeal;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setNumDemand(String numDemand) {
        this.numDemand = numDemand;
    }

    public String getNumDemand() {
        return numDemand;
    }

    public void setNumGotDeal(String numGotDeal) {
        this.numGotDeal = numGotDeal;
    }

    public String getNumGotDeal() {
        return numGotDeal;
    }

    public void setNumSupply(String numSupply) {
        this.numSupply = numSupply;
    }

    public String getNumSupply() {
        return numSupply;
    }
}
