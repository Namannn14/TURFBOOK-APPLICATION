package com.example.turfbook;

import com.example.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoriteAdapter.OnRemoveFavoriteListener {

    private RecyclerView rvFavoritesList;
    private LinearLayout layoutEmptyFavorites;
    private MaterialButton btnEmptyFavExplore;
    private BottomNavigationView bottomNavigation;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        initViews();
        setupRecyclerView();
        setupListeners();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_favorites);
        loadFavorites();
    }

    private void initViews() {
        rvFavoritesList = findViewById(R.id.rvFavoritesList);
        layoutEmptyFavorites = findViewById(R.id.layoutEmptyFavorites);
        btnEmptyFavExplore = findViewById(R.id.btnEmptyFavExplore);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecyclerView() {
        rvFavoritesList.setLayoutManager(new LinearLayoutManager(this));
        favoriteAdapter = new FavoriteAdapter(this, new ArrayList<>(), this);
        rvFavoritesList.setAdapter(favoriteAdapter);
    }

    private void setupListeners() {
        btnEmptyFavExplore.setOnClickListener(v -> {
            startActivity(new Intent(FavoritesActivity.this, HomeActivity.class));
            finish();
        });
    }

    private void loadFavorites() {
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            layoutEmptyFavorites.setVisibility(View.VISIBLE);
            rvFavoritesList.setVisibility(View.GONE);
            return;
        }

        List<Venue> favVenues = databaseHelper.getFavoriteVenues(userId);
        favoriteAdapter.updateList(favVenues);

        if (favVenues.isEmpty()) {
            layoutEmptyFavorites.setVisibility(View.VISIBLE);
            rvFavoritesList.setVisibility(View.GONE);
        } else {
            layoutEmptyFavorites.setVisibility(View.GONE);
            rvFavoritesList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRemoveFavorite(Venue venue) {
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            databaseHelper.removeFavorite(userId, venue.getId());
            Toast.makeText(this, venue.getName() + " removed from favorites", Toast.LENGTH_SHORT).show();
            loadFavorites();
        }
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_favorites);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_favorites) {
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(FavoritesActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(FavoritesActivity.this, SearchActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(FavoritesActivity.this, BookingsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(FavoritesActivity.this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
