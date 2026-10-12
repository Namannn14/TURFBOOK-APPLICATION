package com.example.turfbook;

import com.example.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton btnEditProfileBack;
    private TextInputLayout tilEditName, tilEditMobile, tilEditEmail;
    private TextInputEditText etEditName, etEditMobile, etEditEmail;
    private MaterialButton btnSaveProfile;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        initViews();
        loadExistingData();
        setupListeners();
    }

    private void initViews() {
        btnEditProfileBack = findViewById(R.id.btnEditProfileBack);
        tilEditName = findViewById(R.id.tilEditName);
        tilEditMobile = findViewById(R.id.tilEditMobile);
        tilEditEmail = findViewById(R.id.tilEditEmail);
        etEditName = findViewById(R.id.etEditName);
        etEditMobile = findViewById(R.id.etEditMobile);
        etEditEmail = findViewById(R.id.etEditEmail);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
    }

    private void loadExistingData() {
        etEditName.setText(sessionManager.getUserName());
        etEditMobile.setText(sessionManager.getUserMobile());
        etEditEmail.setText(sessionManager.getUserEmail());
    }

    private void setupListeners() {
        btnEditProfileBack.setOnClickListener(v -> finish());
        btnSaveProfile.setOnClickListener(v -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        String name = etEditName.getText() != null ? etEditName.getText().toString().trim() : "";
        String mobile = etEditMobile.getText() != null ? etEditMobile.getText().toString().trim() : "";
        String email = etEditEmail.getText() != null ? etEditEmail.getText().toString().trim() : "";

        tilEditName.setError(null);
        tilEditMobile.setError(null);
        tilEditEmail.setError(null);

        if (TextUtils.isEmpty(name)) {
            tilEditName.setError("Name cannot be empty.");
            etEditName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mobile) || mobile.length() < 10) {
            tilEditMobile.setError("Please enter a valid 10-digit mobile number.");
            etEditMobile.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEditEmail.setError("Please enter a valid email address.");
            etEditEmail.requestFocus();
            return;
        }

        if (userId != -1) {
            boolean success = databaseHelper.updateUserProfile(userId, name, mobile, email);
            if (success) {
                sessionManager.updateUserSession(name, email, mobile);
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update profile. Email might already be taken.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
