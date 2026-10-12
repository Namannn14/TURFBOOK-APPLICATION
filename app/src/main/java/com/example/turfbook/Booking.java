package com.example.turfbook;

public class Booking {
    private int id;
    private String bookingId;
    private int userId;
    private int venueId;
    private String venueName;
    private String venueLocation;
    private String date;
    private String timeSlot;
    private double amount;
    private String status; // "Confirmed" or "Cancelled"
    private String createdAt;

    public Booking() {
    }

    public Booking(int id, String bookingId, int userId, int venueId, String venueName,
                   String venueLocation, String date, String timeSlot, double amount,
                   String status, String createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.userId = userId;
        this.venueId = venueId;
        this.venueName = venueName;
        this.venueLocation = venueLocation;
        this.date = date;
        this.timeSlot = timeSlot;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Booking(String bookingId, int userId, int venueId, String venueName,
                   String venueLocation, String date, String timeSlot, double amount,
                   String status, String createdAt) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.venueId = venueId;
        this.venueName = venueName;
        this.venueLocation = venueLocation;
        this.date = date;
        this.timeSlot = timeSlot;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueLocation() {
        return venueLocation;
    }

    public void setVenueLocation(String venueLocation) {
        this.venueLocation = venueLocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
