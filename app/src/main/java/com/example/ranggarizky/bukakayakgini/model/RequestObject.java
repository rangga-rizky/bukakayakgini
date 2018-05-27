package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/15/2017.
 */
public class RequestObject {
    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("jumlah_join")
    @Expose
    private String jumlah_join;

    @SerializedName("jumlah")
    @Expose
    private String jumlah;

    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("kategori")
    @Expose
    private String kategori ;

    @SerializedName("secret")
    @Expose
    private String secret ;


    @SerializedName("id")
    @Expose
    private String id ;


    @SerializedName("harga")
    @Expose
    private String harga ;

    @SerializedName("kondisi")
    @Expose
    private String kondisi ;

    @SerializedName("id_buyer")
    @Expose
    private String id_buyer ;

    @SerializedName("status_join")
    @Expose
    private String status_join ;



    @SerializedName("tanggal")
    @Expose
    private String tanggal ;

    @SerializedName("sisa_waktu")
    @Expose
    private String expired_at ;

    @SerializedName("kategori_detail")
    @Expose
    private Kategori_detail kategori_detail ;

    @SerializedName("induk")
    @Expose
    private RequestObject induk;

    @SerializedName("user")
    @Expose
    private User user;


    @SerializedName("supplies")
    @Expose
    private ArrayList<Tawaran> supplies =  new ArrayList<>();


    @SerializedName("foto")
    @Expose
    private String foto ;

    @SerializedName("status_caption")
    @Expose
    private String status_caption ;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("jumlah_anak")
    @Expose
    private int jumlah_anak;

    public void setJumlah_anak(int jumlah_anak) {
        this.jumlah_anak = jumlah_anak;
    }

    public int getJumlah_anak() {
        return jumlah_anak;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setKategori_detail(Kategori_detail kategori_detail) {
        this.kategori_detail = kategori_detail;
    }

    public void setInduk(RequestObject induk) {
        this.induk = induk;
    }

    public RequestObject getInduk() {
        return induk;
    }

    public Kategori_detail getKategori_detail() {
        return kategori_detail;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public String getId_user() {
        return id_user;
    }

    public void setSupplies(ArrayList<Tawaran> supplies) {
        this.supplies = supplies;
    }

    public ArrayList<Tawaran> getSupplies() {
        return supplies;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public String getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }

    public String getKondisi() {
        return kondisi;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStatus_caption(String status_caption) {
        this.status_caption = status_caption;
    }

    public String getStatus_caption() {
        return status_caption;
    }

    public void setJumlah_join(String jumlah_join) {
        this.jumlah_join = jumlah_join;
    }

    public String getJumlah_join() {
        return jumlah_join;
    }

    public void setId_buyer(String id_buyer) {
        this.id_buyer = id_buyer;
    }

    public String getId_buyer() {
        return id_buyer;
    }

    public void setStatus_join(String status_join) {
        this.status_join = status_join;
    }

    public String getStatus_join() {
        return status_join;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }
}
