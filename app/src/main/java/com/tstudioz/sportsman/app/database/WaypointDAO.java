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
    public List<Waypoint> getAllWaypointsByWorkoutID(int workoutID) {
        List<Waypoint> waypoints;
        Cursor cursor = db.query(WaypointContract.WaypointEntry.TABLE_NAME, null,
                SELECTION_WAYPOINTS_BY_WORKOUT_ID,
                new String[] {Integer.toString(workoutID)}, null, null, null);
        long id;
        double speed, distance, lat, lng;
        LatLng latLng;
        waypoints = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getLong(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_WORKOUT_ID));
            speed = cursor.getDouble(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_SPEED));
            distance = cursor.getDouble(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_DISTANCE));
            lat = cursor.getDouble(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_LATITUDE));
            lng = cursor.getDouble(cursor.getColumnIndex(WaypointContract.WaypointEntry.COLUMN_NAME_LONGITUDE));
            waypoints.add(new Waypoint(id, workoutID, speed, distance, new LatLng(lat, lng)));
        }
        return waypoints;
    }

    /**
     * Writes all waypoints in the list to database.
     * @param waypoints list of waypoints to be added to database
     */
    public void add(List<Waypoint> waypoints) {
        ContentValues contentValues;
        db.beginTransaction();
        Waypoint current;
        Iterator<Waypoint> iterator = waypoints.iterator();
        while (iterator.hasNext()) {
            contentValues = new ContentValues();
            current = iterator.next();
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_WORKOUT_ID, current.getWorkoutID());
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_SPEED, current.getSpeed());
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_DISTANCE, current.getDistance());
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_LATITUDE, current.getLatLng().latitude);
            contentValues.put(WaypointContract.WaypointEntry.COLUMN_NAME_LONGITUDE, current.getLatLng().longitude);
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    private static final String SELECTION_WAYPOINT_ID = WaypointContract.WaypointEntry._ID + " = ?";
    /**
     * Deletes all waypoints in the list from database.
     * @param waypoints list of waypoints to be deleted from database
     */
    public void delete(List<Waypoint> waypoints) {
        String[] selectionArgs = new String[waypoints.size()];
        Iterator<Waypoint> iterator = waypoints.iterator();
        for (int i = 0; iterator.hasNext(); )
            selectionArgs[i] = Long.toString(iterator.next().getWorkoutID());
        db.delete(WorkoutContract.WorkoutEntry.TABLE_NAME, SELECTION_WAYPOINT_ID, selectionArgs);
    }
}
