package com.example.turfbook;

public class TimeSlot {
    private String slotText;
    private String period; // "Morning", "Afternoon", "Evening"
    private boolean isBooked;
    private boolean isSelected;

    public TimeSlot(String slotText, String period, boolean isBooked) {
        this.slotText = slotText;
        this.period = period;
        this.isBooked = isBooked;
        this.isSelected = false;
    }

    public String getSlotText() {
        return slotText;
    }

    public void setSlotText(String slotText) {
        this.slotText = slotText;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
