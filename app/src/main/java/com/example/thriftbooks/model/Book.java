package com.example.thriftbooks.model;

import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("author")
    private String author;
    @SerializedName("price")
    private double price;
    @SerializedName("originalPrice")
    private double originalPrice;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("rating")
    private float rating;
    @SerializedName("reviewCount")
    private int reviewCount;
    @SerializedName("condition")
    private String condition;

    public Book(String id, String title, String author, double price, double originalPrice, String imageUrl, float rating, int reviewCount, String condition) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.condition = condition;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public double getOriginalPrice() { return originalPrice; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getCondition() { return condition; }

    public String getDiscountPercentage() {
        if (originalPrice <= 0) return "";
        int discount = (int) ((1 - (price / originalPrice)) * 100);
        return discount > 0 ? discount + "% off" : "";
    }
}
