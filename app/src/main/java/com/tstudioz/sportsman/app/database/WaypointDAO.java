package com.tstudioz.sportsman.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.tstudioz.sportsman.app.training.Waypoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class WaypointDAO extends AbstractDAO {

    public WaypointDAO(Context context) {
        super(context, true);
    }

    private static final String SELECTION_WAYPOINTS_BY_WORKOUT_ID = WaypointContract.WaypointEntry.COLUMN_NAME_WORKOUT_ID + " = ?";
    /**
     * Retrieves all waypoints that belong to specified workoutID.
     * @param workoutID id of the workout
     * @return list of waypoints with specified workoutID
     */
    public List<Waypoint> getAllWaypointsByWorkoutID(long workoutID) {
        List<Waypoint> waypoints;
        Cursor cursor = db.query(WaypointContract.WaypointEntry.TABLE_NAME, null,
                SELECTION_WAYPOINTS_BY_WORKOUT_ID,
                new String[] {Long.toString(workoutID)}, null, null, null);
        long duration;
        float speed, distance;
        double lat, lng;
        String datetime;
        waypoints = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            datetime = cursor.getString(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_DATETIME));
            duration = cursor.getLong(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_DURATION));
            speed = cursor.getFloat(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_SPEED));
            distance = cursor.getFloat(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_DISTANCE));
            lat = cursor.getDouble(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_LATITUDE));
            lng = cursor.getDouble(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_LONGITUDE));
            waypoints.add(new Waypoint(datetime, duration, speed, distance, new LatLng(lat, lng)));
            cursor.moveToNext();
        }
        return waypoints;
    }

    /**
     * Writes all waypoints in the list to database.
     * @param waypoints list of waypoints to be added to database
     */
    public void add(long workoutID, List<Waypoint> waypoints) {
        ContentValues contentValues;
        db.beginTransaction();
        Waypoint current;
        Iterator<Waypoint> iterator = waypoints.iterator();
        while (iterator.hasNext()) {
            contentValues = new ContentValues();
            current = iterator.next();
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_DATETIME, current.getDatetime());
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_DURATION, current.getDuration());
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_WORKOUT_ID, workoutID);
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_SPEED, current.getSpeed());
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_DISTANCE, current.getDistance());
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_LATITUDE, current.getLatLng().latitude);
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_LONGITUDE, current.getLatLng().longitude);
            db.insert(WaypointContract.WaypointEntry.TABLE_NAME, null, contentValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private static final String SELECTION_WORKOUT_ID = WaypointContract.WaypointEntry.COLUMN_NAME_WORKOUT_ID + " = ?";
    /**
     * Deletes all waypoints with the specified workoutID from database.
     * @param workoutID id of the workout
     */
    public int delete(long workoutID) {
        return db.delete(WaypointContract.WaypointEntry.TABLE_NAME, SELECTION_WORKOUT_ID,
                new String[] {String.valueOf(workoutID)});
    }
}
