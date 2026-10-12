package com.example.turfbook;

import com.example.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ImageButton btnSearchBack, btnClearSearch;
    private EditText etSearchQuery;
    private ChipGroup chipGroupSports, chipGroupPriceRating;
    private Chip chipSportAll, chipPriceUnder500, chipPrice500To1000, chipPriceAbove1000, chipRating4Plus, chipRating3Plus;
    private TextView tvSearchResultsCount, btnResetFilters;
    private RecyclerView rvSearchResults;
    private LinearLayout layoutEmptySearch;
    private BottomNavigationView bottomNavigation;

    private DatabaseHelper databaseHelper;
    private VenueAdapter venueAdapter;

    private String selectedSport = "All";
    private Double maxPrice = null;
    private Double minRating = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        databaseHelper = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        setupFilters();
        setupListeners();
        setupBottomNavigation();
        performSearch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_search);
        performSearch();
    }

    private void initViews() {
        btnSearchBack = findViewById(R.id.btnSearchBack);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        etSearchQuery = findViewById(R.id.etSearchQuery);
        chipGroupSports = findViewById(R.id.chipGroupSports);
        chipGroupPriceRating = findViewById(R.id.chipGroupPriceRating);
        chipSportAll = findViewById(R.id.chipSportAll);
        chipPriceUnder500 = findViewById(R.id.chipPriceUnder500);
        chipPrice500To1000 = findViewById(R.id.chipPrice500To1000);
        chipPriceAbove1000 = findViewById(R.id.chipPriceAbove1000);
        chipRating4Plus = findViewById(R.id.chipRating4Plus);
        chipRating3Plus = findViewById(R.id.chipRating3Plus);
        tvSearchResultsCount = findViewById(R.id.tvSearchResultsCount);
        btnResetFilters = findViewById(R.id.btnResetFilters);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        layoutEmptySearch = findViewById(R.id.layoutEmptySearch);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecyclerView() {
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        venueAdapter = new VenueAdapter(this, new ArrayList<>());
        rvSearchResults.setAdapter(venueAdapter);
    }

    private void setupFilters() {
        chipGroupSports.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                selectedSport = "All";
                chipSportAll.setChecked(true);
            } else {
                int checkedId = checkedIds.get(0);
                Chip chip = findViewById(checkedId);
                if (chip != null) {
                    String text = chip.getText().toString();
                    selectedSport = text.equalsIgnoreCase("All Sports") ? "All" : text;
                }
            }
            performSearch();
        });

        chipPriceUnder500.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipPrice500To1000.setChecked(false);
                chipPriceAbove1000.setChecked(false);
                maxPrice = 500.0;
            } else if (!chipPrice500To1000.isChecked() && !chipPriceAbove1000.isChecked()) {
                maxPrice = null;
            }
            performSearch();
        });

        chipPrice500To1000.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipPriceUnder500.setChecked(false);
                chipPriceAbove1000.setChecked(false);
                maxPrice = 1000.0;
            } else if (!chipPriceUnder500.isChecked() && !chipPriceAbove1000.isChecked()) {
                maxPrice = null;
            }
            performSearch();
        });

        chipPriceAbove1000.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipPriceUnder500.setChecked(false);
                chipPrice500To1000.setChecked(false);
                maxPrice = 2000.0;
            } else if (!chipPriceUnder500.isChecked() && !chipPrice500To1000.isChecked()) {
                maxPrice = null;
            }
            performSearch();
        });

        chipRating4Plus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipRating3Plus.setChecked(false);
                minRating = 4.0;
            } else if (!chipRating3Plus.isChecked()) {
                minRating = null;
            }
            performSearch();
        });

        chipRating3Plus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipRating4Plus.setChecked(false);
                minRating = 3.0;
            } else if (!chipRating4Plus.isChecked()) {
                minRating = null;
            }
            performSearch();
        });
    }

    private void setupListeners() {
        btnSearchBack.setOnClickListener(v -> finish());

        btnClearSearch.setOnClickListener(v -> {
            etSearchQuery.setText("");
            btnClearSearch.setVisibility(View.GONE);
            performSearch();
        });

        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnClearSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnResetFilters.setOnClickListener(v -> {
            etSearchQuery.setText("");
            selectedSport = "All";
            chipSportAll.setChecked(true);
            chipPriceUnder500.setChecked(false);
            chipPrice500To1000.setChecked(false);
            chipPriceAbove1000.setChecked(false);
            chipRating4Plus.setChecked(false);
            chipRating3Plus.setChecked(false);
            maxPrice = null;
            minRating = null;
            performSearch();
        });
    }

    private void performSearch() {
        String query = etSearchQuery.getText() != null ? etSearchQuery.getText().toString().trim() : "";
        List<Venue> results = databaseHelper.searchVenues(query, selectedSport, maxPrice, minRating);

        venueAdapter.updateList(results);
        tvSearchResultsCount.setText("Showing " + results.size() + " venues");

        if (results.isEmpty()) {
            layoutEmptySearch.setVisibility(View.VISIBLE);
            rvSearchResults.setVisibility(View.GONE);
        } else {
            layoutEmptySearch.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.VISIBLE);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_search);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_search) {
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(SearchActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(SearchActivity.this, BookingsActivity.class));
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(SearchActivity.this, FavoritesActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}
