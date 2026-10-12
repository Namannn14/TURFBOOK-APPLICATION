package com.example.turfbook;

import com.example.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileAvatarInitial, tvProfileName, tvProfileEmail, tvProfileMobile;
    private LinearLayout rowEditProfile, rowMyBookings, rowFavoriteVenues, rowHelpSupport, rowAboutApp;
    private MaterialCardView rowLogout;
    private BottomNavigationView bottomNavigation;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        initViews();
        setupUserData();
        setupListeners();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
        setupUserData();
    }

    private void initViews() {
        tvProfileAvatarInitial = findViewById(R.id.tvProfileAvatarInitial);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfileMobile = findViewById(R.id.tvProfileMobile);

        rowEditProfile = findViewById(R.id.rowEditProfile);
        rowMyBookings = findViewById(R.id.rowMyBookings);
        rowFavoriteVenues = findViewById(R.id.rowFavoriteVenues);
        rowHelpSupport = findViewById(R.id.rowHelpSupport);
        rowAboutApp = findViewById(R.id.rowAboutApp);
        rowLogout = findViewById(R.id.rowLogout);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupUserData() {
        String name = sessionManager.getUserName();
        String email = sessionManager.getUserEmail();
        String mobile = sessionManager.getUserMobile();

        tvProfileName.setText(name != null && !name.isEmpty() ? name : "User");
        tvProfileEmail.setText(email != null && !email.isEmpty() ? email : "No email set");
        tvProfileMobile.setText(mobile != null && !mobile.isEmpty() ? "+91 " + mobile : "+91 9876543210");

        if (name != null && !name.isEmpty()) {
            tvProfileAvatarInitial.setText(String.valueOf(name.charAt(0)).toUpperCase());
        } else {
            tvProfileAvatarInitial.setText("U");
        }
    }

    private void setupListeners() {
        rowEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

        rowMyBookings.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, BookingsActivity.class));
        });

        rowFavoriteVenues.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, FavoritesActivity.class));
        });

        rowHelpSupport.setOnClickListener(v -> showHelpSupportDialog());

        rowAboutApp.setOnClickListener(v -> showAboutDialog());

        rowLogout.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void showHelpSupportDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help & Support")
                .setMessage("TurfBook Support Center\n\n"
                        + "• Customer Support: support@turfbook.example.com\n"
                        + "• Helpline: +91 98765 43210 (9 AM - 9 PM)\n"
                        + "• Location: Chiplun, Maharashtra\n\n"
                        + "For slot cancellations, please navigate to My Bookings and tap Cancel Booking.")
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About TurfBook")
                .setMessage("TurfBook – Local Turf / Sports Venue Booking App\n\n"
                        + "Version: 1.0 (MAD Project Edition)\n"
                        + "Platform: Android (Java + XML + SQLite)\n"
                        + "Developed for: Mobile Application Development (MAD) College Curriculum\n\n"
                        + "Features:\n"
                        + "✓ SQLite Local Database Storage\n"
                        + "✓ Real-time Time Slot Conflict Prevention\n"
                        + "✓ Filter by Sport, Price, & Rating\n"
                        + "✓ Instant Booking Confirmation\n"
                        + "✓ Session Management via SharedPreferences")
                .setPositiveButton("Awesome", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout from TurfBook?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    sessionManager.logout();
                    Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(ProfileActivity.this, BookingsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(ProfileActivity.this, FavoritesActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
