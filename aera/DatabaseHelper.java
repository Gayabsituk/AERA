package com.example.aera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "aera.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ACTIVITY = "activities";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_SUBTYPE = "subtype";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_CO2E = "co2e";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ACTIVITY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_SUBTYPE + " TEXT, " +
                    COLUMN_VALUE + " REAL, " +
                    COLUMN_CO2E + " REAL, " +
                    COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        onCreate(db);
    }

    public long addActivity(String type, String subtype, double value, double co2e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_SUBTYPE, subtype);
        values.put(COLUMN_VALUE, value);
        values.put(COLUMN_CO2E, co2e);
        return db.insert(TABLE_ACTIVITY, null, values);
    }

    public List<ActivityEntry> getAllActivities() {
        List<ActivityEntry> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACTIVITY, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ActivityEntry entry = new ActivityEntry(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBTYPE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VALUE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CO2E)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                );
                list.add(entry);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public double getTotalFootprint() {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;
        // Fixed to use correct table name (activities) and column name (co2e)
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_CO2E + ") FROM " + TABLE_ACTIVITY, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        }
        db.close();
        return total;
    }

    public double getTodayTotalCO2() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_CO2E + ") FROM " + TABLE_ACTIVITY +
                " WHERE date(" + COLUMN_TIMESTAMP + ") = date('now', 'localtime')", null);
        double total = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        }
        db.close();
        return total;
    }
}