# TurfBook – Local Sports Venue & Turf Booking App ⚽🏏🏸

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=flat&logo=java&logoColor=white)](https://www.java.com/)
[![UI](https://img.shields.io/badge/UI-Native%20XML%20%7C%20Material%20Design%203-0F5132?style=flat)](https://material.io/)
[![Database](https://img.shields.io/badge/Database-SQLite%20(Local)-003B57?style=flat&logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24%20(Android%207.0)-blue?style=flat)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-MIT-green?style=flat)](LICENSE)

> A native Android application for discovering, checking real-time slot availability, and booking local sports turfs and arenas (Cricket, Football, Badminton, Basketball, Futsal, Tennis) with offline-first SQLite persistence and zero booking conflicts. Built for the **Mobile Application Development (MAD)** curriculum.

---

## 📌 Project Overview

**TurfBook** simplifies the process of reserving sports turfs in local communities and towns. Instead of manual phone calls and scheduling clashes, players can browse nearby venues, inspect amenities (floodlights, parking, locker rooms), check available hourly time slots for any date, and receive instant booking confirmations.

All data—user accounts, pre-seeded local venues, bookings, and favorites—is managed on-device using a robust **SQLite** database and **SharedPreferences** session management without requiring third-party cloud backends.

---

## ✨ Key Features

- **🔐 User Authentication & Session Management**:
  - Secure registration with client-side validation (email format, 10-digit mobile number, minimum 6-character password).
  - Login system with persistent session handling via `SharedPreferences`.
  - Automatic routing from `SplashActivity` based on login status.
  - Pre-seeded test accounts for instant one-click demonstration.

- **🏟️ Turf & Venue Discovery**:
  - Browse popular local turfs with high-resolution vector stadium illustrations.
  - Quick sports category filter on Home: **All, Cricket, Football, Badminton, Basketball, Futsal, Tennis**.
  - Detailed venue profiles showing price per hour, star rating, verified review count, location, description, and available amenities.

- **🔍 Smart Search & Multi-Criteria Filtering**:
  - Real-time text search across venue names, sports, and locations.
  - Interactive Filter Chips by Sport.
  - Price filters (**Under ₹500**, **₹500 – ₹1000**, **Above ₹1000**).
  - Rating filters (**4+ Stars**, **3+ Stars**).
  - Instant filter reset and empty-state messaging.

- **⏱️ Real-Time Time Slot Availability & Conflict Engine**:
  - Interactive native `DatePickerDialog` (past dates automatically disabled).
  - 1-hour time slots categorized into **Morning (6 AM - 10 AM)**, **Afternoon (12 PM - 4 PM)**, and **Evening (5 PM - 10 PM)**.
  - **Zero Double-Booking Guarantee**: The app queries SQLite (`isSlotBooked()`) in real time. Already booked slots are marked with a distinct badge and disabled from selection.

- **🧾 Digital Ticket Confirmation**:
  - Generates formatted, unique Booking IDs (e.g., `TB20261015842`).
  - Transparent cost breakdown: Base Turf Rate + Platform/Convenience Fee (₹50) = Total Amount.
  - Simulated payment processing confirmation screen with direct navigation options.

- **📅 Booking Management & Cancellation**:
  - Categorized into **Upcoming** and **Past / Cancelled** bookings via `TabLayout`.
  - Cancel active bookings with a confirmation dialog.
  - **Instant Slot Release**: Cancelling an upcoming booking immediately marks the slot as available in SQLite for other players to reserve.

- **❤️ Favorite Venues**:
  - Save preferred courts and turfs with one tap on the heart icon.
  - Dedicated Favorites screen for quick re-booking with instant removal options.

- **👤 User Profile & Customization**:
  - View user profile with personalized avatar initials.
  - Edit Profile activity to update Name, Mobile Number, and Email.
  - Help & Support and About App modals.
  - Secure logout clearing the local session.

---

## 🛠️ Tech Stack & Architecture

| Component | Technology |
|---|---|
| **Language** | Java (100% Native) |
| **UI Framework** | Android XML Layouts, Material Design 3 Components (`com.google.android.material`) |
| **Architecture** | Model-View-Adapter (MVA) / MVC |
| **Database** | SQLite via native `SQLiteOpenHelper` |
| **Session Store** | `SharedPreferences` |
| **List Components** | `RecyclerView`, `GridLayoutManager`, `LinearLayoutManager` |
| **Minimum SDK** | API 24 (Android 7.0 Nougat) |
| **Target SDK** | API 36 |

---

## 🗄️ Database Schema (`turfbook.db`)

The local SQLite database implements 4 relational tables:

```sql
-- Users Table
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    email TEXT UNIQUE,
    mobile TEXT,
    password TEXT
);

-- Venues Table
CREATE TABLE venues (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    location TEXT,
    sport TEXT,
    price REAL,
    rating REAL,
    reviewCount INTEGER,
    description TEXT,
    facilities TEXT,
    image TEXT
);

-- Bookings Table
CREATE TABLE bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    bookingId TEXT UNIQUE,
    userId INTEGER,
    venueId INTEGER,
    venueName TEXT,
    venueLocation TEXT,
    date TEXT,
    timeSlot TEXT,
    amount REAL,
    status TEXT, -- 'Confirmed' | 'Cancelled'
    createdAt TEXT
);

-- Favorites Table
CREATE TABLE favorites (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER,
    venueId INTEGER
);
```

---

## 🔑 Demo Login Credentials

The database is pre-seeded with ready-to-use demo accounts:

| Role | Email (ID) | Password | Name | Mobile |
|---|---|---|---|---|
| **Primary Demo** | `user@turfbook.com` | `password123` | Naman Dugar | 9876543210 |
| **Secondary Demo**| `demo@turfbook.com` | `123456` | Rahul Sharma | 9820012345 |

*(New user accounts can also be created via the registration screen.)*

---

## 🚀 How to Run in Android Studio

1. **Clone or Download the Repository**:
   ```bash
   git clone https://github.com/<your-username>/TurfBook.git
   ```
2. **Open Project**:
   - Open **Android Studio** (Hedgehog, Iguana, Jellyfish, or newer).
   - Select **File > Open...** and select the cloned directory.
3. **Sync Gradle**:
   - Allow Android Studio to sync the dependencies from `build.gradle.kts`.
4. **Run the App**:
   - Select an emulator (AVD with API 24+) or connect an Android device with USB Debugging enabled.
   - Click the green **Run (▶)** button or press `Shift + F10`.

---

## 📂 Project Architecture & Package Structure

```text
com.example.turfbook/
├── models/
│   ├── User.java                   # User entity
│   ├── Venue.java                  # Venue entity
│   ├── Booking.java                # Booking entity
│   ├── Favorite.java               # Favorite mapping entity
│   ├── TimeSlot.java               # Slot selection state model
│   └── SportCategory.java          # Category filter model
├── database/
│   ├── DatabaseHelper.java         # SQLiteOpenHelper (CRUD & seeding)
│   └── SessionManager.java         # SharedPreferences session wrapper
├── adapters/
│   ├── VenueAdapter.java           # Venue cards list adapter
│   ├── BookingAdapter.java         # Bookings & cancel action adapter
│   ├── FavoriteAdapter.java        # Saved favorites adapter
│   ├── SportCategoryAdapter.java   # Horizontal sports filter adapter
│   └── TimeSlotAdapter.java        # Morning/Afternoon/Evening slot grid adapter
└── activities/
    ├── SplashActivity.java         # Animated launcher & session gate
    ├── LoginActivity.java          # Authentication with pre-fill demo
    ├── RegisterActivity.java       # User account creation with validation
    ├── HomeActivity.java           # Dashboard, categories & nearby venues
    ├── SearchActivity.java         # Search & multi-filter chip engine
    ├── VenueDetailsActivity.java   # Full venue profile & amenities
    ├── BookingActivity.java        # DatePicker & slot conflict prevention
    ├── BookingConfirmationActivity.java # Digital receipt & booking ID
    ├── BookingsActivity.java       # Upcoming/Past bookings & cancellation
    ├── FavoritesActivity.java      # Saved venues screen
    ├── ProfileActivity.java        # User profile & options
    └── EditProfileActivity.java    # Update profile details
```

---

## 🔮 Future Enhancements

- [ ] Online Payment Gateway integration (Razorpay / UPI).
- [ ] Push notifications for booking reminders 1 hour before slot time.
- [ ] Live Google Maps GPS navigation to venue location.
- [ ] Turf Owner / Admin Dashboard to manage venue pricing and schedule maintenance blocks.
- [ ] User ratings and photo reviews submission.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

Developed as part of the **Mobile Application Development (MAD)** college project curriculum.
