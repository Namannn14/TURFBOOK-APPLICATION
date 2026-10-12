package com.example.turfbook;

import com.example.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView tvHomeGreeting, tvUserInitial, tvHomeVenueCount;
    private MaterialCardView cardHomeSearch, btnQuickProfile;
    private MaterialButton btnBannerBookNow;
    private RecyclerView rvPopularSports, rvHomeVenues;
    private BottomNavigationView bottomNavigation;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private VenueAdapter venueAdapter;
    private SportCategoryAdapter sportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        initViews();
        setupUserData();
        setupSportsRecyclerView();
        setupVenuesRecyclerView();
        setupBottomNavigation();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUserData();
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        loadVenues("All");
    }

    private void initViews() {
        tvHomeGreeting = findViewById(R.id.tvHomeGreeting);
        tvUserInitial = findViewById(R.id.tvUserInitial);
        tvHomeVenueCount = findViewById(R.id.tvHomeVenueCount);
        cardHomeSearch = findViewById(R.id.cardHomeSearch);
        btnQuickProfile = findViewById(R.id.btnQuickProfile);
        btnBannerBookNow = findViewById(R.id.btnBannerBookNow);
        rvPopularSports = findViewById(R.id.rvPopularSports);
        rvHomeVenues = findViewById(R.id.rvHomeVenues);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupUserData() {
        String name = sessionManager.getUserName();
        tvHomeGreeting.setText("Hello, " + name + " 👋");
        if (name != null && !name.isEmpty()) {
            tvUserInitial.setText(String.valueOf(name.charAt(0)).toUpperCase());
        } else {
            tvUserInitial.setText("U");
        }
    }

    private void setupSportsRecyclerView() {
        List<SportCategory> sports = new ArrayList<>();
        sports.add(new SportCategory("All", R.drawable.ic_sport_all, "All"));
        sports.add(new SportCategory("Cricket", R.drawable.ic_sport_cricket, "Cricket"));
        sports.add(new SportCategory("Football", R.drawable.ic_sport_football, "Football"));
        sports.add(new SportCategory("Badminton", R.drawable.ic_sport_badminton, "Badminton"));
        sports.add(new SportCategory("Basketball", R.drawable.ic_sport_basketball, "Basketball"));
        sports.add(new SportCategory("Futsal", R.drawable.ic_sport_futsal, "Futsal"));
        sports.add(new SportCategory("Tennis", R.drawable.ic_sport_tennis, "Tennis"));

        sportAdapter = new SportCategoryAdapter(this, sports, category -> loadVenues(category.getSportKey()));
        rvPopularSports.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPopularSports.setAdapter(sportAdapter);
    }

    private void setupVenuesRecyclerView() {
        rvHomeVenues.setLayoutManager(new LinearLayoutManager(this));
        venueAdapter = new VenueAdapter(this, new ArrayList<>());
        rvHomeVenues.setAdapter(venueAdapter);
        loadVenues("All");
    }

    private void loadVenues(String sport) {
        List<Venue> venues = databaseHelper.getVenuesBySport(sport);
        venueAdapter.updateList(venues);
        tvHomeVenueCount.setText(venues.size() + " Venues");
    }

    private void setupListeners() {
        cardHomeSearch.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        btnQuickProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnBannerBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(HomeActivity.this, BookingsActivity.class));
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(HomeActivity.this, FavoritesActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}
