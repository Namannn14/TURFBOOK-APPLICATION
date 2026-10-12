package com.example.turfbook;

import com.example.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BookingsActivity extends AppCompatActivity implements BookingAdapter.OnBookingCancelListener {

    private TabLayout tabLayoutBookings;
    private RecyclerView rvBookingsList;
    private LinearLayout layoutEmptyBookings;
    private MaterialButton btnEmptyExploreTurfs;
    private BottomNavigationView bottomNavigation;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private BookingAdapter bookingAdapter;

    private boolean isUpcomingTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        initViews();
        setupRecyclerView();
        setupTabs();
        setupListeners();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_bookings);
        loadBookings();
    }

    private void initViews() {
        tabLayoutBookings = findViewById(R.id.tabLayoutBookings);
        rvBookingsList = findViewById(R.id.rvBookingsList);
        layoutEmptyBookings = findViewById(R.id.layoutEmptyBookings);
        btnEmptyExploreTurfs = findViewById(R.id.btnEmptyExploreTurfs);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecyclerView() {
        rvBookingsList.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(this, new ArrayList<>(), this);
        rvBookingsList.setAdapter(bookingAdapter);
    }

    private void setupTabs() {
        tabLayoutBookings.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isUpcomingTab = (tab.getPosition() == 0);
                loadBookings();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupListeners() {
        btnEmptyExploreTurfs.setOnClickListener(v -> {
            startActivity(new Intent(BookingsActivity.this, HomeActivity.class));
            finish();
        });
    }

    private void loadBookings() {
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            layoutEmptyBookings.setVisibility(View.VISIBLE);
            rvBookingsList.setVisibility(View.GONE);
            return;
        }

        List<Booking> bookings;
        if (isUpcomingTab) {
            bookings = databaseHelper.getUpcomingBookings(userId);
        } else {
            bookings = databaseHelper.getPastBookings(userId);
        }

        bookingAdapter.updateList(bookings);

        if (bookings.isEmpty()) {
            layoutEmptyBookings.setVisibility(View.VISIBLE);
            rvBookingsList.setVisibility(View.GONE);
        } else {
            layoutEmptyBookings.setVisibility(View.GONE);
            rvBookingsList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCancelBooking(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel your booking for " + booking.getVenueName() + " on " + booking.getDate() + " (" + booking.getTimeSlot() + ")?")
                .setPositiveButton("Confirm Cancellation", (dialog, which) -> {
                    boolean success = databaseHelper.cancelBooking(booking.getBookingId());
                    if (success) {
                        Toast.makeText(this, "Booking cancelled successfully. Slot is now available again.", Toast.LENGTH_SHORT).show();
                        loadBookings();
                    } else {
                        Toast.makeText(this, "Failed to cancel booking. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Keep Booking", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_bookings);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_bookings) {
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(BookingsActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(BookingsActivity.this, SearchActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(BookingsActivity.this, FavoritesActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(BookingsActivity.this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
