package com.example.turfbook;

public class Venue {
    private int id;
    private String name;
    private String location;
    private String sport;
    private double price;
    private double rating;
    private int reviewCount;
    private String description;
    private String facilities;
    private String image;

    public Venue() {
    }

    public Venue(int id, String name, String location, String sport, double price,
                 double rating, int reviewCount, String description, String facilities, String image) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.sport = sport;
        this.price = price;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.description = description;
        this.facilities = facilities;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
