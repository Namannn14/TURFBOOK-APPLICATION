package com.example.turfbook;

import com.example.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class BookingConfirmationActivity extends AppCompatActivity {

    private TextView tvConfirmBookingId, tvConfirmVenueName, tvConfirmDate, tvConfirmTime, tvConfirmAmount;
    private MaterialButton btnConfirmViewBookings, btnConfirmBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        initViews();
        displayData();
        setupListeners();
    }

    private void initViews() {
        tvConfirmBookingId = findViewById(R.id.tvConfirmBookingId);
        tvConfirmVenueName = findViewById(R.id.tvConfirmVenueName);
        tvConfirmDate = findViewById(R.id.tvConfirmDate);
        tvConfirmTime = findViewById(R.id.tvConfirmTime);
        tvConfirmAmount = findViewById(R.id.tvConfirmAmount);
        btnConfirmViewBookings = findViewById(R.id.btnConfirmViewBookings);
        btnConfirmBackHome = findViewById(R.id.btnConfirmBackHome);
    }

    private void displayData() {
        String bookingId = getIntent().getStringExtra("booking_id");
        String venueName = getIntent().getStringExtra("venue_name");
        String date = getIntent().getStringExtra("date");
        String timeSlot = getIntent().getStringExtra("time_slot");
        double amount = getIntent().getDoubleExtra("amount", 0.0);

        tvConfirmBookingId.setText(bookingId != null ? bookingId : "TB20261015001");
        tvConfirmVenueName.setText(venueName != null ? venueName : "Turf Venue");
        tvConfirmDate.setText(date != null ? date : "");
        tvConfirmTime.setText(timeSlot != null ? timeSlot : "");
        tvConfirmAmount.setText(String.format("₹%.0f", amount));
    }

    private void setupListeners() {
        btnConfirmViewBookings.setOnClickListener(v -> {
            Intent intent = new Intent(BookingConfirmationActivity.this, BookingsActivity.class);
            startActivity(intent);
            finish();
        });

        btnConfirmBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(BookingConfirmationActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookingConfirmationActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
