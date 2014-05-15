package com.tstudioz.sportsman.app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Sportsman.db";

    private static final String CREATE_TABLE_WORKOUTS = "CREATE TABLE " + WorkoutContract.WorkoutEntry.TABLE_NAME
            + " (" + WorkoutContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + WorkoutContract.WorkoutEntry.COLUMN_NAME_DISTANCE + " TEXT, "
            + WorkoutContract.WorkoutEntry.COLUMN_NAME_DURATION + " INTEGER, "
            + WorkoutContract.WorkoutEntry.COLUMN_NAME_SPORT_ID + " INTEGER, "
            + WorkoutContract.WorkoutEntry.COLUMN_NAME_MOOD_ID + " INTEGER, "
            + WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME + " TIMESTAMP)";

    private static final String CREATE_TABLE_WAYPOINTS = "CREATE TABLE " + WaypointContract.WaypointEntry.TABLE_NAME
            + " (" + WaypointContract.WaypointEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + WaypointContract.WaypointEntry.COLUMN_NAME_WORKOUT_ID + " INTEGER, "
            + WaypointContract.WaypointEntry.COLUMN_NAME_DATETIME + " TIMESTAMP, "
            + WorkoutContract.WorkoutEntry.COLUMN_NAME_DURATION + " INTEGER, "
            + WaypointContract.WaypointEntry.COLUMN_NAME_SPEED + " TEXT, "
            + WaypointContract.WaypointEntry.COLUMN_NAME_DISTANCE + " TEXT, "
            + WaypointContract.WaypointEntry.COLUMN_NAME_LATITUDE + " TEXT, "
            + WaypointContract.WaypointEntry.COLUMN_NAME_LONGITUDE + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORKOUTS);
        db.execSQL(CREATE_TABLE_WAYPOINTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + WorkoutContract.WorkoutEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WaypointContract.WaypointEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static Cursor getAll(SQLiteDatabase db, String tableName) {
        return db.rawQuery("SELECT * FROM " + tableName, null);
    }
}
