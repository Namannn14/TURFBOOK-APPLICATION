package com.example.turfbook;

import com.example.R;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BookingActivity extends AppCompatActivity {

    private ImageButton btnBookingBack;
    private TextView tvBookingVenueTitle, tvBookingVenueLocation, tvBookingVenueRate;
    private MaterialCardView cardSelectDate;
    private TextView tvSelectedDateDisplay, tvSelectedSlotIndicator;
    private MaterialButton btnPickDate, btnConfirmBooking;
    private RecyclerView rvMorningSlots, rvAfternoonSlots, rvEveningSlots;
    private TextView tvSummaryBasePrice, tvSummaryBookingFee, tvSummaryTotalPrice;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Venue venue;
    private int venueId = -1;

    private Calendar selectedCalendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dbDateFormat;
    private String selectedDateFormatted = "";
    private String selectedSlotText = null;

    private List<TimeSlot> morningSlots = new ArrayList<>();
    private List<TimeSlot> afternoonSlots = new ArrayList<>();
    private List<TimeSlot> eveningSlots = new ArrayList<>();

    private TimeSlotAdapter morningAdapter;
    private TimeSlotAdapter afternoonAdapter;
    private TimeSlotAdapter eveningAdapter;

    private static final double BOOKING_FEE = 50.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        venueId = getIntent().getIntExtra("venue_id", -1);
        if (venueId == -1) {
            Toast.makeText(this, "Venue not selected.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        venue = databaseHelper.getVenueById(venueId);
        if (venue == null) {
            Toast.makeText(this, "Venue unavailable.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        selectedCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDateFormatted = dateFormat.format(selectedCalendar.getTime());

        initViews();
        setupVenueData();
        setupRecyclerViews();
        setupListeners();
        loadSlotsForCurrentDate();
    }

    private void initViews() {
        btnBookingBack = findViewById(R.id.btnBookingBack);
        tvBookingVenueTitle = findViewById(R.id.tvBookingVenueTitle);
        tvBookingVenueLocation = findViewById(R.id.tvBookingVenueLocation);
        tvBookingVenueRate = findViewById(R.id.tvBookingVenueRate);
        cardSelectDate = findViewById(R.id.cardSelectDate);
        tvSelectedDateDisplay = findViewById(R.id.tvSelectedDateDisplay);
        tvSelectedSlotIndicator = findViewById(R.id.tvSelectedSlotIndicator);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        rvMorningSlots = findViewById(R.id.rvMorningSlots);
        rvAfternoonSlots = findViewById(R.id.rvAfternoonSlots);
        rvEveningSlots = findViewById(R.id.rvEveningSlots);
        tvSummaryBasePrice = findViewById(R.id.tvSummaryBasePrice);
        tvSummaryBookingFee = findViewById(R.id.tvSummaryBookingFee);
        tvSummaryTotalPrice = findViewById(R.id.tvSummaryTotalPrice);
    }

    private void setupVenueData() {
        tvBookingVenueTitle.setText(venue.getName());
        tvBookingVenueLocation.setText(venue.getLocation() + ", Maharashtra");
        tvBookingVenueRate.setText(String.format("₹%.0f/hr", venue.getPrice()));
        tvSelectedDateDisplay.setText(selectedDateFormatted);

        tvSummaryBasePrice.setText(String.format("₹%.0f", venue.getPrice()));
        tvSummaryBookingFee.setText(String.format("₹%.0f", BOOKING_FEE));
        double total = venue.getPrice() + BOOKING_FEE;
        tvSummaryTotalPrice.setText(String.format("₹%.0f", total));
    }

    private void setupRecyclerViews() {
        rvMorningSlots.setLayoutManager(new GridLayoutManager(this, 2));
        rvAfternoonSlots.setLayoutManager(new GridLayoutManager(this, 2));
        rvEveningSlots.setLayoutManager(new GridLayoutManager(this, 2));

        morningAdapter = new TimeSlotAdapter(this, morningSlots, this::onSlotClicked);
        afternoonAdapter = new TimeSlotAdapter(this, afternoonSlots, this::onSlotClicked);
        eveningAdapter = new TimeSlotAdapter(this, eveningSlots, this::onSlotClicked);

        rvMorningSlots.setAdapter(morningAdapter);
        rvAfternoonSlots.setAdapter(afternoonAdapter);
        rvEveningSlots.setAdapter(eveningAdapter);
    }

    private void setupListeners() {
        btnBookingBack.setOnClickListener(v -> finish());
        btnPickDate.setOnClickListener(v -> showDatePicker());
        cardSelectDate.setOnClickListener(v -> showDatePicker());

        btnConfirmBooking.setOnClickListener(v -> processBooking());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    selectedDateFormatted = dateFormat.format(selectedCalendar.getTime());
                    tvSelectedDateDisplay.setText(selectedDateFormatted);

                    // Reset selected slot
                    selectedSlotText = null;
                    tvSelectedSlotIndicator.setText("None Selected");

                    // Reload slot availability for new date
                    loadSlotsForCurrentDate();
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Disallow past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void loadSlotsForCurrentDate() {
        morningSlots.clear();
        afternoonSlots.clear();
        eveningSlots.clear();

        String dateKey = selectedDateFormatted;

        // Morning slots
        addSlotWithBookingCheck(morningSlots, "6:00 AM - 7:00 AM", "Morning", dateKey);
        addSlotWithBookingCheck(morningSlots, "7:00 AM - 8:00 AM", "Morning", dateKey);
        addSlotWithBookingCheck(morningSlots, "8:00 AM - 9:00 AM", "Morning", dateKey);
        addSlotWithBookingCheck(morningSlots, "9:00 AM - 10:00 AM", "Morning", dateKey);

        // Afternoon slots
        addSlotWithBookingCheck(afternoonSlots, "12:00 PM - 1:00 PM", "Afternoon", dateKey);
        addSlotWithBookingCheck(afternoonSlots, "1:00 PM - 2:00 PM", "Afternoon", dateKey);
        addSlotWithBookingCheck(afternoonSlots, "2:00 PM - 3:00 PM", "Afternoon", dateKey);
        addSlotWithBookingCheck(afternoonSlots, "3:00 PM - 4:00 PM", "Afternoon", dateKey);

        // Evening slots
        addSlotWithBookingCheck(eveningSlots, "5:00 PM - 6:00 PM", "Evening", dateKey);
        addSlotWithBookingCheck(eveningSlots, "6:00 PM - 7:00 PM", "Evening", dateKey);
        addSlotWithBookingCheck(eveningSlots, "7:00 PM - 8:00 PM", "Evening", dateKey);
        addSlotWithBookingCheck(eveningSlots, "8:00 PM - 9:00 PM", "Evening", dateKey);
        addSlotWithBookingCheck(eveningSlots, "9:00 PM - 10:00 PM", "Evening", dateKey);

        morningAdapter.notifyDataSetChanged();
        afternoonAdapter.notifyDataSetChanged();
        eveningAdapter.notifyDataSetChanged();
    }

    private void addSlotWithBookingCheck(List<TimeSlot> list, String slotTime, String period, String date) {
        boolean isBooked = databaseHelper.isSlotBooked(venueId, date, slotTime);
        list.add(new TimeSlot(slotTime, period, isBooked));
    }

    private void onSlotClicked(TimeSlot clickedSlot) {
        if (clickedSlot.isBooked()) {
            Toast.makeText(this, "This slot is already booked.", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedSlotText = clickedSlot.getSlotText();
        tvSelectedSlotIndicator.setText(selectedSlotText);

        // Deselect all other slots across all adapters
        updateSlotSelection(morningSlots, clickedSlot);
        updateSlotSelection(afternoonSlots, clickedSlot);
        updateSlotSelection(eveningSlots, clickedSlot);

        morningAdapter.notifyDataSetChanged();
        afternoonAdapter.notifyDataSetChanged();
        eveningAdapter.notifyDataSetChanged();
    }

    private void updateSlotSelection(List<TimeSlot> slots, TimeSlot target) {
        for (TimeSlot slot : slots) {
            slot.setSelected(slot.equals(target));
        }
    }

    private void processBooking() {
        if (selectedDateFormatted.isEmpty()) {
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSlotText == null || selectedSlotText.isEmpty()) {
            Toast.makeText(this, "Please select a time slot.", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Please log in to confirm booking.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BookingActivity.this, LoginActivity.class));
            return;
        }

        // Final check against database
        if (databaseHelper.isSlotBooked(venueId, selectedDateFormatted, selectedSlotText)) {
            Toast.makeText(this, "This time slot is already booked. Please choose another.", Toast.LENGTH_LONG).show();
            loadSlotsForCurrentDate();
            return;
        }

        double totalAmount = venue.getPrice() + BOOKING_FEE;
        String bookingCode = generateBookingId();
        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        Booking booking = new Booking(
                bookingCode,
                userId,
                venueId,
                venue.getName(),
                venue.getLocation(),
                selectedDateFormatted,
                selectedSlotText,
                totalAmount,
                "Confirmed",
                createdAt
        );

        boolean success = databaseHelper.addBooking(booking);
        if (success) {
            Intent intent = new Intent(BookingActivity.this, BookingConfirmationActivity.class);
            intent.putExtra("booking_id", bookingCode);
            intent.putExtra("venue_name", venue.getName());
            intent.putExtra("date", selectedDateFormatted);
            intent.putExtra("time_slot", selectedSlotText);
            intent.putExtra("amount", totalAmount);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Booking failed. Slot may have been reserved.", Toast.LENGTH_LONG).show();
            loadSlotsForCurrentDate();
        }
    }

    private String generateBookingId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String datePart = sdf.format(selectedCalendar.getTime());
        int randomDigits = 100 + new Random().nextInt(900);
        return "TB" + datePart + randomDigits;
    }
}
