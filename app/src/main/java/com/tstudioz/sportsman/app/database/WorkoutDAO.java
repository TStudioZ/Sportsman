package com.tstudioz.sportsman.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tstudioz.sportsman.app.training.Sport;
import com.tstudioz.sportsman.app.training.Waypoint;
import com.tstudioz.sportsman.app.training.Workout;
import com.tstudioz.sportsman.app.training.WorkoutWithWaypoints;

import java.util.List;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class WorkoutDAO extends AbstractDAO {

    private Context context;

    public WorkoutDAO(Context context) {
        super(context, true);
        this.context = context;
    }

    /**
     * Returns a new workout with all information including
     * list of waypoints.
     * @param id id of the workout
     * @return workout with list of waypoints
     */
    public WorkoutWithWaypoints getWorkout(long id) {
        WaypointDAO waypointDAO = new WaypointDAO(context);
        List<Waypoint> waypoints = waypointDAO.getAllWaypointsByWorkoutID(id);
        Cursor cursor = db.rawQuery("SELECT * FROM " + WorkoutContract.WorkoutEntry.TABLE_NAME
                + " WHERE " + WorkoutContract.WorkoutEntry._ID + " = ?",
                new String[]{String.valueOf(id)});

        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        long duration = cursor.getLong(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DURATION));
        float distance = cursor.getFloat(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DISTANCE));
        int mood = cursor.getInt(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_MOOD_ID));
        Sport sport = Sport.getSport(cursor.getInt(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_SPORT_ID)));
        String datetime = cursor.getString(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME));

        return new WorkoutWithWaypoints(datetime, duration, distance, mood, sport, waypoints);
    }

    /**
     * Creates new content values based on workout.
     * @param workout workout with info
     * @return ContentValues filled with workout info
     */
    private ContentValues getContentValues(Workout workout) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_DISTANCE, workout.getDistance());
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_DURATION, workout.getDuration());
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_SPORT_ID, workout.getSport().getId());
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_MOOD_ID, workout.getMood());
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME, workout.getDatetime());
        return contentValues;
    }

    /**
     * Adds new workout to database.
     * @param workout workout to be added
     * @return id of the new workout
     */
    public long add(Workout workout) {
        return db.insert(WorkoutContract.WorkoutEntry.TABLE_NAME, null, getContentValues(workout));
    }

    /**
     * Tries to update workout with the specified id.
     * If none is found, then it adds a new workout to the database.
     * @param workout workout info
     * @param workoutID id of the workout being updated
     */
    public void updateAdd(Workout workout, long workoutID) {
        if ( db.update(WorkoutContract.WorkoutEntry.TABLE_NAME, getContentValues(workout),
                WorkoutContract.WorkoutEntry._ID + " = ?", new String[] {String.valueOf(workoutID)}) == 0 )
            add(workout);
    }

    /**
     * Adds new workout and waypoints to database.
     * @param workout workout to be added
     * @return id of the new workout
     */
    public long add(WorkoutWithWaypoints workout) {
        long id = add((Workout) workout);
        new WaypointDAO(context).add(id, workout.getWaypoints());
        return id;
    }

    private static final String SELECTION_WORKOUT_ID = WorkoutContract.WorkoutEntry._ID + " = ?";
    /**
     * Deletes a workout with the same id as the one\'s in parameter
     * and all waypoints with it.
     * @param workoutID workout id to be deleted
     */
    public int delete(long workoutID) {
        String[] selectionArgs = { String.valueOf(workoutID) };
        int w = new WaypointDAO(context).delete(workoutID);
        return db.delete(WorkoutContract.WorkoutEntry.TABLE_NAME, SELECTION_WORKOUT_ID, selectionArgs);
    }

    private static final String SELECTION_WORKOUTS_LAST_WEEK = " BETWEEN datetime('00:00', '-6 days') AND datetime('now', 'localtime')";
    public Cursor getLastWeek() {
        return db.rawQuery("SELECT * FROM " + WorkoutContract.WorkoutEntry.TABLE_NAME + " WHERE " +
                        WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME + SELECTION_WORKOUTS_LAST_WEEK,
                null);
    }

    private static final String SELECTION_WORKOUTS_THIS_MONTH = " BETWEEN datetime('00:00', 'start of month') AND datetime('now', 'localtime')";
    public Cursor getThisMonth() {
        return db.rawQuery("SELECT * FROM " + WorkoutContract.WorkoutEntry.TABLE_NAME + " WHERE " +
                        WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME + SELECTION_WORKOUTS_THIS_MONTH +
                        " ORDER BY " + WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME + " DESC",
                null);
    }

    private static final String SELECT_COUNT = "SELECT * FROM " + WorkoutContract.WorkoutEntry.TABLE_NAME + " WHERE " + WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME;

    public int getNumOfWorkouts() {
        Cursor cursor = db.rawQuery(SELECT_COUNT + SELECTION_WORKOUTS_THIS_MONTH, null);
        return cursor.getCount();
    }

    public int getNumOfWorkouts(int sportID) {
        Cursor cursor = db.rawQuery(SELECT_COUNT + SELECTION_WORKOUTS_THIS_MONTH
                + " AND " + WorkoutContract.WorkoutEntry.COLUMN_NAME_SPORT_ID + " = ?",
                new String[] {String.valueOf(sportID)});
        return cursor.getCount();
    }

    public Cursor getAll() {
        return DatabaseHelper.getAll(db, WorkoutContract.WorkoutEntry.TABLE_NAME);
    }
}
