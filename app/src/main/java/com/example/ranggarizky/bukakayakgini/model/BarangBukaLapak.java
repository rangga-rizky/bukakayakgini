package com.example.ranggarizky.bukakayakgini.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/17/2017.
 */
public class BarangBukaLapak {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("stock")
    @Expose
    private String stock;


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("condition")
    @Expose
    private String condition;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("rating")
    @Expose
    private Rating rating;

    @SerializedName("seller_name")
    @Expose
    private String seller_name;

    @SerializedName("city")
    @Expose
    private String city;

        @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("seller_level")
    @Expose
    private String seller_level;

    @SerializedName("seller_avatar")
    @Expose
    private String seller_avatar;

    @SerializedName("seller_level_badge_url")
    @Expose
    private String seller_level_badge_url;



    @SerializedName("images")
    @Expose
    private ArrayList<String> images = new ArrayList<>();



    @SerializedName("seller_id")
    @Expose
    private String seller_id;

    @SerializedName("small_images")
    @Expose
    private ArrayList<String> small_images = new ArrayList<>();

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<String> getSmall_images() {
        return small_images;
    }

    public Rating getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setSmall_images(ArrayList<String> small_images) {
        this.small_images = small_images;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setSeller_avatar(String seller_avatar) {
        this.seller_avatar = seller_avatar;
    }

    public String getSeller_avatar() {
        return seller_avatar;
    }

    public void setSeller_level(String seller_level) {
        this.seller_level = seller_level;
    }

    public String getSeller_level() {
        return seller_level;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStock() {
        return stock;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_level_badge_url(String seller_level_badge_url) {
        this.seller_level_badge_url = seller_level_badge_url;
    }

    public String getSeller_level_badge_url() {
        return seller_level_badge_url;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
