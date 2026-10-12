package com.example.turfbook;

public class SportCategory {
    private String name;
    private int iconResId;
    private String sportKey;

    public SportCategory(String name, int iconResId, String sportKey) {
        this.name = name;
        this.iconResId = iconResId;
        this.sportKey = sportKey;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getSportKey() {
        return sportKey;
    }
}
