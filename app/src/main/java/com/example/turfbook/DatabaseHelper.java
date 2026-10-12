package com.example.turfbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "turfbook.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_MOBILE = "mobile";
    public static final String COL_USER_PASSWORD = "password";

    // Venues table
    public static final String TABLE_VENUES = "venues";
    public static final String COL_VENUE_ID = "id";
    public static final String COL_VENUE_NAME = "name";
    public static final String COL_VENUE_LOCATION = "location";
    public static final String COL_VENUE_SPORT = "sport";
    public static final String COL_VENUE_PRICE = "price";
    public static final String COL_VENUE_RATING = "rating";
    public static final String COL_VENUE_REVIEW_COUNT = "reviewCount";
    public static final String COL_VENUE_DESCRIPTION = "description";
    public static final String COL_VENUE_FACILITIES = "facilities";
    public static final String COL_VENUE_IMAGE = "image";

    // Bookings table
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String COL_BOOKING_ID = "id";
    public static final String COL_BOOKING_CODE = "bookingId";
    public static final String COL_BOOKING_USER_ID = "userId";
    public static final String COL_BOOKING_VENUE_ID = "venueId";
    public static final String COL_BOOKING_VENUE_NAME = "venueName";
    public static final String COL_BOOKING_VENUE_LOCATION = "venueLocation";
    public static final String COL_BOOKING_DATE = "date";
    public static final String COL_BOOKING_TIME_SLOT = "timeSlot";
    public static final String COL_BOOKING_AMOUNT = "amount";
    public static final String COL_BOOKING_STATUS = "status";
    public static final String COL_BOOKING_CREATED_AT = "createdAt";

    // Favorites table
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COL_FAV_ID = "id";
    public static final String COL_FAV_USER_ID = "userId";
    public static final String COL_FAV_VENUE_ID = "venueId";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_NAME + " TEXT, "
                + COL_USER_EMAIL + " TEXT UNIQUE, "
                + COL_USER_MOBILE + " TEXT, "
                + COL_USER_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Venues Table
        String createVenuesTable = "CREATE TABLE " + TABLE_VENUES + " ("
                + COL_VENUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_VENUE_NAME + " TEXT, "
                + COL_VENUE_LOCATION + " TEXT, "
                + COL_VENUE_SPORT + " TEXT, "
                + COL_VENUE_PRICE + " REAL, "
                + COL_VENUE_RATING + " REAL, "
                + COL_VENUE_REVIEW_COUNT + " INTEGER, "
                + COL_VENUE_DESCRIPTION + " TEXT, "
                + COL_VENUE_FACILITIES + " TEXT, "
                + COL_VENUE_IMAGE + " TEXT)";
        db.execSQL(createVenuesTable);

        // Create Bookings Table
        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS + " ("
                + COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_BOOKING_CODE + " TEXT UNIQUE, "
                + COL_BOOKING_USER_ID + " INTEGER, "
                + COL_BOOKING_VENUE_ID + " INTEGER, "
                + COL_BOOKING_VENUE_NAME + " TEXT, "
                + COL_BOOKING_VENUE_LOCATION + " TEXT, "
                + COL_BOOKING_DATE + " TEXT, "
                + COL_BOOKING_TIME_SLOT + " TEXT, "
                + COL_BOOKING_AMOUNT + " REAL, "
                + COL_BOOKING_STATUS + " TEXT, "
                + COL_BOOKING_CREATED_AT + " TEXT)";
        db.execSQL(createBookingsTable);

        // Create Favorites Table
        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " ("
                + COL_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FAV_USER_ID + " INTEGER, "
                + COL_FAV_VENUE_ID + " INTEGER)";
        db.execSQL(createFavoritesTable);

        // Seed Sample Venues
        seedSampleVenues(db);

        // Seed Sample Users
        seedSampleUsers(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        seedSampleUsers(db);
    }

    private void seedSampleUsers(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ?",
                new String[]{"user@turfbook.com"});
        if (cursor == null || cursor.getCount() == 0) {
            ContentValues user1 = new ContentValues();
            user1.put(COL_USER_NAME, "Naman Dugar");
            user1.put(COL_USER_EMAIL, "user@turfbook.com");
            user1.put(COL_USER_MOBILE, "9876543210");
            user1.put(COL_USER_PASSWORD, "password123");
            db.insert(TABLE_USERS, null, user1);
        }
        if (cursor != null) cursor.close();

        Cursor cursor2 = db.rawQuery("SELECT id FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ?",
                new String[]{"demo@turfbook.com"});
        if (cursor2 == null || cursor2.getCount() == 0) {
            ContentValues user2 = new ContentValues();
            user2.put(COL_USER_NAME, "Rahul Sharma");
            user2.put(COL_USER_EMAIL, "demo@turfbook.com");
            user2.put(COL_USER_MOBILE, "9820012345");
            user2.put(COL_USER_PASSWORD, "123456");
            db.insert(TABLE_USERS, null, user2);
        }
        if (cursor2 != null) cursor2.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENUES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void seedSampleVenues(SQLiteDatabase db) {
        insertVenueSeed(db, "Champion Turf", "Chiplun", "Cricket", 800.0, 4.6, 128,
                "Champion Turf is a premier sports venue in Chiplun with international standard artificial turf, ideal for cricket and football tournaments. Fully equipped with floodlights, player dugout, and spectator seating.",
                "Parking,Flood Lights,Changing Room,Drinking Water,Seating Area",
                "img_turf_champion");

        insertVenueSeed(db, "Green Arena", "Chiplun", "Football", 1000.0, 4.7, 95,
                "Green Arena offers FIFA-standard 7v7 artificial grass football turf with tournament-grade LED floodlighting, hygienic washrooms, locker rooms, and refreshments area.",
                "Parking,Flood Lights,Washroom,Changing Room,Drinking Water,Seating Area",
                "img_turf_green_arena");

        insertVenueSeed(db, "Smash Zone", "Chiplun", "Badminton", 500.0, 4.5, 84,
                "Smash Zone features high-grip BWF approved synthetic wooden courts with anti-glare overhead lighting, tournament gear, and coaching facilities.",
                "Changing Room,Drinking Water,Washroom,Parking",
                "img_turf_smash_zone");

        insertVenueSeed(db, "Victory Sports Arena", "Chiplun", "Cricket", 750.0, 4.4, 110,
                "Victory Sports Arena offers dual-lane box cricket turfs with safety netting, bowling machine support, and live score display board.",
                "Parking,Flood Lights,Drinking Water,Seating Area,Washroom",
                "img_turf_victory");

        insertVenueSeed(db, "PowerPlay Turf", "Chiplun", "Football", 900.0, 4.3, 67,
                "PowerPlay Turf provides state-of-the-art 5-a-side and 6-a-side football facilities with soft rubber infill turf for optimal traction and injury prevention.",
                "Flood Lights,Changing Room,Washroom,Drinking Water",
                "img_turf_powerplay");

        insertVenueSeed(db, "Ace Badminton Court", "Chiplun", "Badminton", 450.0, 4.6, 142,
                "Indoor badminton stadium with Yonex synthetic mats, well-ventilated halls, racket stringing support, and spacious locker rooms.",
                "Parking,Drinking Water,Washroom,Changing Room",
                "img_turf_ace_badminton");

        insertVenueSeed(db, "Hoops Arena", "Chiplun", "Basketball", 700.0, 4.5, 53,
                "Full-court basketball facility with polyurethane outdoor cushioning, shock-absorbing rims, and bright night match illumination.",
                "Flood Lights,Drinking Water,Parking,Washroom",
                "img_turf_hoops");

        insertVenueSeed(db, "Sports Hub", "Chiplun", "Multi-sport", 850.0, 4.4, 76,
                "All-in-one multi-sport complex catering to cricket, football, futsal, and volleyball with dedicated dugout spaces and parking.",
                "Parking,Flood Lights,Washroom,Changing Room,Drinking Water,Seating Area",
                "img_turf_sports_hub");

        insertVenueSeed(db, "Coastal Futsal Park", "Chiplun", "Futsal", 950.0, 4.8, 89,
                "High-intensity covered futsal arena with shockpad underlay, electronic scoreboard, and pro sound system for corporate and college leagues.",
                "Flood Lights,Changing Room,Drinking Water,Parking,Washroom",
                "img_turf_green_arena");

        insertVenueSeed(db, "Chiplun Tennis Club", "Chiplun", "Tennis", 650.0, 4.3, 41,
                "Synthetic hard court tennis facility with professional coaching, night floodlights, and peaceful surroundings.",
                "Flood Lights,Parking,Changing Room,Drinking Water",
                "img_turf_smash_zone");
    }

    private void insertVenueSeed(SQLiteDatabase db, String name, String location, String sport,
                                 double price, double rating, int reviewCount, String description,
                                 String facilities, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_VENUE_NAME, name);
        values.put(COL_VENUE_LOCATION, location);
        values.put(COL_VENUE_SPORT, sport);
        values.put(COL_VENUE_PRICE, price);
        values.put(COL_VENUE_RATING, rating);
        values.put(COL_VENUE_REVIEW_COUNT, reviewCount);
        values.put(COL_VENUE_DESCRIPTION, description);
        values.put(COL_VENUE_FACILITIES, facilities);
        values.put(COL_VENUE_IMAGE, image);
        db.insert(TABLE_VENUES, null, values);
    }

    // -------------------------------------------------------------
    // USER AUTHENTICATION & PROFILE METHODS
    // -------------------------------------------------------------

    public boolean registerUser(String name, String email, String mobile, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email.trim().toLowerCase());
        values.put(COL_USER_MOBILE, mobile.trim());
        values.put(COL_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USER_EMAIL + " = ?", new String[]{email.trim().toLowerCase()},
                null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        return exists;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_EMAIL + " = ? AND " + COL_USER_PASSWORD + " = ?",
                new String[]{email.trim().toLowerCase(), password},
                null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_MOBILE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD))
            );
            cursor.close();
        }
        return user;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_ID + " = ?", new String[]{String.valueOf(userId)},
                null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_MOBILE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD))
            );
            cursor.close();
        }
        return user;
    }

    public boolean updateUserProfile(int userId, String name, String mobile, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_MOBILE, mobile);
        values.put(COL_USER_EMAIL, email.trim().toLowerCase());

        int rows = db.update(TABLE_USERS, values, COL_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // -------------------------------------------------------------
    // VENUE CRUD & QUERY METHODS
    // -------------------------------------------------------------

    public List<Venue> getAllVenues() {
        List<Venue> venueList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VENUES + " ORDER BY " + COL_VENUE_RATING + " DESC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                venueList.add(mapCursorToVenue(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return venueList;
    }

    public List<Venue> getVenuesBySport(String sport) {
        List<Venue> venueList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if (sport == null || sport.equalsIgnoreCase("All")) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_VENUES + " ORDER BY " + COL_VENUE_RATING + " DESC", null);
        } else {
            cursor = db.query(TABLE_VENUES, null,
                    COL_VENUE_SPORT + " LIKE ?", new String[]{"%" + sport + "%"},
                    null, null, COL_VENUE_RATING + " DESC");
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                venueList.add(mapCursorToVenue(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return venueList;
    }

    public List<Venue> searchVenues(String query, String sport, Double maxPrice, Double minRating) {
        List<Venue> venueList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_VENUES + " WHERE 1=1");
        List<String> args = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND (").append(COL_VENUE_NAME).append(" LIKE ? OR ")
               .append(COL_VENUE_LOCATION).append(" LIKE ? OR ")
               .append(COL_VENUE_SPORT).append(" LIKE ?)");
            String wildcard = "%" + query.trim() + "%";
            args.add(wildcard);
            args.add(wildcard);
            args.add(wildcard);
        }

        if (sport != null && !sport.equalsIgnoreCase("All") && !sport.equalsIgnoreCase("All Sports")) {
            sql.append(" AND ").append(COL_VENUE_SPORT).append(" LIKE ?");
            args.add("%" + sport + "%");
        }

        if (maxPrice != null && maxPrice > 0) {
            sql.append(" AND ").append(COL_VENUE_PRICE).append(" <= ?");
            args.add(String.valueOf(maxPrice));
        }

        if (minRating != null && minRating > 0) {
            sql.append(" AND ").append(COL_VENUE_RATING).append(" >= ?");
            args.add(String.valueOf(minRating));
        }

        sql.append(" ORDER BY ").append(COL_VENUE_RATING).append(" DESC");

        Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                venueList.add(mapCursorToVenue(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return venueList;
    }

    public Venue getVenueById(int venueId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VENUES, null,
                COL_VENUE_ID + " = ?", new String[]{String.valueOf(venueId)},
                null, null, null);

        Venue venue = null;
        if (cursor != null && cursor.moveToFirst()) {
            venue = mapCursorToVenue(cursor);
            cursor.close();
        }
        return venue;
    }

    private Venue mapCursorToVenue(Cursor cursor) {
        return new Venue(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_VENUE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_VENUE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_VENUE_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_VENUE_SPORT)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_VENUE_PRICE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_VENUE_RATING)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_VENUE_REVIEW_COUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_VENUE_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_VENUE_FACILITIES)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_VENUE_IMAGE))
        );
    }

    // -------------------------------------------------------------
    // BOOKING AVAILABILITY & CRUD METHODS
    // -------------------------------------------------------------

    public boolean isSlotBooked(int venueId, String date, String timeSlot) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, new String[]{COL_BOOKING_ID},
                COL_BOOKING_VENUE_ID + " = ? AND " +
                COL_BOOKING_DATE + " = ? AND " +
                COL_BOOKING_TIME_SLOT + " = ? AND " +
                COL_BOOKING_STATUS + " = ?",
                new String[]{String.valueOf(venueId), date, timeSlot, "Confirmed"},
                null, null, null);

        boolean booked = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        return booked;
    }

    public boolean addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check availability first
        if (isSlotBooked(booking.getVenueId(), booking.getDate(), booking.getTimeSlot())) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(COL_BOOKING_CODE, booking.getBookingId());
        values.put(COL_BOOKING_USER_ID, booking.getUserId());
        values.put(COL_BOOKING_VENUE_ID, booking.getVenueId());
        values.put(COL_BOOKING_VENUE_NAME, booking.getVenueName());
        values.put(COL_BOOKING_VENUE_LOCATION, booking.getVenueLocation());
        values.put(COL_BOOKING_DATE, booking.getDate());
        values.put(COL_BOOKING_TIME_SLOT, booking.getTimeSlot());
        values.put(COL_BOOKING_AMOUNT, booking.getAmount());
        values.put(COL_BOOKING_STATUS, booking.getStatus());
        values.put(COL_BOOKING_CREATED_AT, booking.getCreatedAt());

        long result = db.insert(TABLE_BOOKINGS, null, values);
        return result != -1;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null,
                COL_BOOKING_USER_ID + " = ?", new String[]{String.valueOf(userId)},
                null, null, COL_BOOKING_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToBooking(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Booking> getUpcomingBookings(int userId) {
        List<Booking> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null,
                COL_BOOKING_USER_ID + " = ? AND " + COL_BOOKING_STATUS + " = ?",
                new String[]{String.valueOf(userId), "Confirmed"},
                null, null, COL_BOOKING_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToBooking(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Booking> getPastBookings(int userId) {
        List<Booking> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null,
                COL_BOOKING_USER_ID + " = ? AND " + COL_BOOKING_STATUS + " != ?",
                new String[]{String.valueOf(userId), "Confirmed"},
                null, null, COL_BOOKING_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToBooking(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public boolean cancelBooking(String bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOKING_STATUS, "Cancelled");

        int rows = db.update(TABLE_BOOKINGS, values,
                COL_BOOKING_CODE + " = ?", new String[]{bookingId});
        return rows > 0;
    }

    private Booking mapCursorToBooking(Cursor cursor) {
        return new Booking(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_CODE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_USER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_VENUE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_VENUE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_VENUE_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_TIME_SLOT)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BOOKING_AMOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_STATUS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_CREATED_AT))
        );
    }

    // -------------------------------------------------------------
    // FAVORITES CRUD METHODS
    // -------------------------------------------------------------

    public boolean isFavorite(int userId, int venueId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, new String[]{COL_FAV_ID},
                COL_FAV_USER_ID + " = ? AND " + COL_FAV_VENUE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(venueId)},
                null, null, null);

        boolean isFav = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        return isFav;
    }

    public boolean addFavorite(int userId, int venueId) {
        if (isFavorite(userId, venueId)) return true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FAV_USER_ID, userId);
        values.put(COL_FAV_VENUE_ID, venueId);
        long result = db.insert(TABLE_FAVORITES, null, values);
        return result != -1;
    }

    public boolean removeFavorite(int userId, int venueId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_FAVORITES,
                COL_FAV_USER_ID + " = ? AND " + COL_FAV_VENUE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(venueId)});
        return rows > 0;
    }

    public List<Venue> getFavoriteVenues(int userId) {
        List<Venue> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT v.* FROM " + TABLE_VENUES + " v INNER JOIN "
                + TABLE_FAVORITES + " f ON v." + COL_VENUE_ID + " = f." + COL_FAV_VENUE_ID
                + " WHERE f." + COL_FAV_USER_ID + " = ? ORDER BY v." + COL_VENUE_RATING + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToVenue(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
