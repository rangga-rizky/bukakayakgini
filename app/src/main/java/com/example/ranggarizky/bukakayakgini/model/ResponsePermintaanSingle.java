package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/18/2017.
 */
public class ResponsePermintaanSingle {
    @SerializedName("data")
    @Expose
    private RequestObject data;

    @SerializedName("userIsJoin")
    @Expose
    private String userIsJoin;



    public void setData(RequestObject data) {
        this.data = data;
    }

    public RequestObject getData() {
        return data;
    }

    public void setUserIsJoin(String userIsJoin) {
        this.userIsJoin = userIsJoin;
    }

    public String getUserIsJoin() {
        return userIsJoin;
    }
}
