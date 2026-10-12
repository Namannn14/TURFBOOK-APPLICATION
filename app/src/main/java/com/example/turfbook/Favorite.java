package com.example.turfbook;

public class Favorite {
    private int id;
    private int userId;
    private int venueId;

    public Favorite() {
    }

    public Favorite(int id, int userId, int venueId) {
        this.id = id;
        this.userId = userId;
        this.venueId = venueId;
    }

    public Favorite(int userId, int venueId) {
        this.userId = userId;
        this.venueId = venueId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }
}
