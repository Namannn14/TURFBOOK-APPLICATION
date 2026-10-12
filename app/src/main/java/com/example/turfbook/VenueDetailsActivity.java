package com.example.turfbook;

import com.example.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class VenueDetailsActivity extends AppCompatActivity {

    private ImageView ivDetailVenueImage, ivDetailFavoriteIcon;
    private MaterialCardView btnDetailBack, btnDetailFavorite;
    private TextView tvDetailSport, tvDetailRating, tvDetailVenueName, tvDetailLocation;
    private TextView tvDetailDescription, tvDetailBottomPrice;
    private MaterialButton btnBookNow;
    private LinearLayout facilityParking, facilityLights, facilityRoom, facilityWater, facilityWashroom, facilitySeating;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Venue venue;
    private int venueId = -1;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        venueId = getIntent().getIntExtra("venue_id", -1);
        if (venueId == -1) {
            Toast.makeText(this, "Venue not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        venue = databaseHelper.getVenueById(venueId);
        if (venue == null) {
            Toast.makeText(this, "Venue details unavailable.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        displayVenueData();
        checkFavoriteStatus();
        setupListeners();
    }

    private void initViews() {
        ivDetailVenueImage = findViewById(R.id.ivDetailVenueImage);
        ivDetailFavoriteIcon = findViewById(R.id.ivDetailFavoriteIcon);
        btnDetailBack = findViewById(R.id.btnDetailBack);
        btnDetailFavorite = findViewById(R.id.btnDetailFavorite);
        tvDetailSport = findViewById(R.id.tvDetailSport);
        tvDetailRating = findViewById(R.id.tvDetailRating);
        tvDetailVenueName = findViewById(R.id.tvDetailVenueName);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailBottomPrice = findViewById(R.id.tvDetailBottomPrice);
        btnBookNow = findViewById(R.id.btnBookNow);

        facilityParking = findViewById(R.id.facilityParking);
        facilityLights = findViewById(R.id.facilityLights);
        facilityRoom = findViewById(R.id.facilityRoom);
        facilityWater = findViewById(R.id.facilityWater);
        facilityWashroom = findViewById(R.id.facilityWashroom);
        facilitySeating = findViewById(R.id.facilitySeating);
    }

    private void displayVenueData() {
        tvDetailVenueName.setText(venue.getName());
        tvDetailLocation.setText(venue.getLocation() + ", Maharashtra");
        tvDetailSport.setText(venue.getSport());
        tvDetailRating.setText(String.format("%.1f (%d reviews)", venue.getRating(), venue.getReviewCount()));
        tvDetailDescription.setText(venue.getDescription());
        tvDetailBottomPrice.setText(String.format("₹%.0f / hour", venue.getPrice()));

        int resId = getResources().getIdentifier(venue.getImage(), "drawable", getPackageName());
        if (resId != 0) {
            ivDetailVenueImage.setImageResource(resId);
        } else {
            ivDetailVenueImage.setImageResource(R.drawable.img_turf_generic);
        }

        // Show/hide facilities based on venue facilities string
        String fac = venue.getFacilities() != null ? venue.getFacilities().toLowerCase() : "";
        facilityParking.setVisibility(fac.contains("parking") ? View.VISIBLE : View.GONE);
        facilityLights.setVisibility(fac.contains("lights") ? View.VISIBLE : View.GONE);
        facilityRoom.setVisibility(fac.contains("room") ? View.VISIBLE : View.GONE);
        facilityWater.setVisibility(fac.contains("water") ? View.VISIBLE : View.GONE);
        facilityWashroom.setVisibility(fac.contains("washroom") ? View.VISIBLE : View.GONE);
        facilitySeating.setVisibility(fac.contains("seating") ? View.VISIBLE : View.GONE);
    }

    private void checkFavoriteStatus() {
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            isFavorite = databaseHelper.isFavorite(userId, venueId);
            updateFavoriteIcon();
        }
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            ivDetailFavoriteIcon.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            ivDetailFavoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void setupListeners() {
        btnDetailBack.setOnClickListener(v -> finish());

        btnDetailFavorite.setOnClickListener(v -> {
            int userId = sessionManager.getUserId();
            if (userId == -1) {
                Toast.makeText(this, "Please login to save favorites.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isFavorite) {
                databaseHelper.removeFavorite(userId, venueId);
                isFavorite = false;
                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            } else {
                databaseHelper.addFavorite(userId, venueId);
                isFavorite = true;
                Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon();
        });

        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(VenueDetailsActivity.this, BookingActivity.class);
            intent.putExtra("venue_id", venueId);
            startActivity(intent);
        });
    }
}
