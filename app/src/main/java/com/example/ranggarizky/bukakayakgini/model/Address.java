package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ranggarizky on 1/19/2017.
 */
public class Address {

    @SerializedName("province")
    @Expose
    private String province;

    @SerializedName("city")
    @Expose
    private String city;

    public void setCity(String city) {
            this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }
}
