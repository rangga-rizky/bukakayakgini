package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by RanggaRizky on 5/23/2017.
 */
public class Notif {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_terkait")
    @Expose
    private String id_terkait;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("other")
    @Expose
    private String other;

    public void setOther(String other) {
        this.other = other;
    }

    public String getOther() {
        return other;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setId_terkait(String id_terkait) {
        this.id_terkait = id_terkait;
    }

    public String getId_terkait() {
        return id_terkait;
    }
}
