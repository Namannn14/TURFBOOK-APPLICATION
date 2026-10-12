package com.example.turfbook;

import com.example.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilRegName, tilRegEmail, tilRegMobile, tilRegPassword, tilRegConfirmPassword;
    private TextInputEditText etRegName, etRegEmail, etRegMobile, etRegPassword, etRegConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvAlreadyHaveAccount;
    private ImageButton btnBackToLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilRegName = findViewById(R.id.tilRegName);
        tilRegEmail = findViewById(R.id.tilRegEmail);
        tilRegMobile = findViewById(R.id.tilRegMobile);
        tilRegPassword = findViewById(R.id.tilRegPassword);
        tilRegConfirmPassword = findViewById(R.id.tilRegConfirmPassword);

        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegMobile = findViewById(R.id.etRegMobile);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> performRegistration());

        btnBackToLogin.setOnClickListener(v -> finish());
        tvAlreadyHaveAccount.setOnClickListener(v -> finish());
    }

    private void performRegistration() {
        String name = etRegName.getText() != null ? etRegName.getText().toString().trim() : "";
        String email = etRegEmail.getText() != null ? etRegEmail.getText().toString().trim() : "";
        String mobile = etRegMobile.getText() != null ? etRegMobile.getText().toString().trim() : "";
        String password = etRegPassword.getText() != null ? etRegPassword.getText().toString().trim() : "";
        String confirmPassword = etRegConfirmPassword.getText() != null ? etRegConfirmPassword.getText().toString().trim() : "";

        // Reset errors
        tilRegName.setError(null);
        tilRegEmail.setError(null);
        tilRegMobile.setError(null);
        tilRegPassword.setError(null);
        tilRegConfirmPassword.setError(null);

        if (TextUtils.isEmpty(name)) {
            tilRegName.setError("Please enter your full name.");
            etRegName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            tilRegEmail.setError("Please enter your email.");
            etRegEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilRegEmail.setError("Please enter a valid email.");
            etRegEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            tilRegMobile.setError("Please enter your mobile number.");
            etRegMobile.requestFocus();
            return;
        }

        if (mobile.length() < 10) {
            tilRegMobile.setError("Please enter a valid 10-digit mobile number.");
            etRegMobile.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tilRegPassword.setError("Please enter a password.");
            etRegPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            tilRegPassword.setError("Password must contain at least 6 characters.");
            etRegPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tilRegConfirmPassword.setError("Please confirm your password.");
            etRegConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            tilRegConfirmPassword.setError("Password and confirm password must match.");
            etRegConfirmPassword.requestFocus();
            return;
        }

        // Check if email already registered
        if (databaseHelper.checkEmailExists(email)) {
            tilRegEmail.setError("This email is already registered. Please sign in.");
            etRegEmail.requestFocus();
            return;
        }

        boolean success = databaseHelper.registerUser(name, email, mobile, password);
        if (success) {
            Toast.makeText(this, "Registration Successful! Please sign in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
