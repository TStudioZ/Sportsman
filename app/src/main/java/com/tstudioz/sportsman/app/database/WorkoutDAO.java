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
    public Workout getWorkout(int id) {
        WaypointDAO waypointDAO = new WaypointDAO(context);
        List<Waypoint> waypoints = waypointDAO.getAllWaypointsByWorkoutID(id);
        Cursor cursor = db.rawQuery("SELECT * FROM " + WorkoutContract.WorkoutEntry.TABLE_NAME
                + " WHERE " + WorkoutContract.WorkoutEntry._ID + " = ?",
                new String[]{String.valueOf(id)});

        if (cursor.getCount() == 0) return null;

        double distance = cursor.getDouble(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DISTANCE));
        long cals = cursor.getLong(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_CALORIES));
        int mood = cursor.getInt(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_MOOD_ID));
        int weather = cursor.getInt(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_WEATHER_ID));
        Sport sport = Sport.newInstance(cursor.getInt(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_SPORT_ID)));

        return new WorkoutWithWaypoints(id, distance, cals, mood, weather, sport, waypoints);
    }

    /**
     * Adds new workout to database.
     * @param workout workout to be added
     * @return id of the new workout
     */
    public long add(Workout workout) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_DISTANCE, workout.getDistance());
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_CALORIES, workout.getCals());
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_MOOD_ID, workout.getMood());
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_WEATHER_ID, workout.getWeather());
        contentValues.put(WorkoutContract.WorkoutEntry.COLUMN_NAME_SPORT_ID, workout.getSport().getId());
        return db.insert(WorkoutContract.WorkoutEntry.TABLE_NAME, null, contentValues);
    }

    /**
     * Adds new workout and waypoints to database.
     * @param workout workout to be added
     * @return id of the new workout
     */
    public long add(WorkoutWithWaypoints workout) {
        long id = add(workout);
        new WaypointDAO(context).add(workout.getWaypoints());
        return id;
    }

    private static final String SELECTION_WORKOUT_ID = WorkoutContract.WorkoutEntry._ID + " = ?";
    /**
     * Deletes a workout with the same id as the one\'s in parameter
     * and all waypoints with it.
     * @param workout workout to be deleted
     */
    public void delete(Workout workout) {
        String[] selectionArgs = { String.valueOf(workout.getId()) };
        db.delete(WorkoutContract.WorkoutEntry.TABLE_NAME, SELECTION_WORKOUT_ID, selectionArgs);
    }

    public Cursor getAll() {
        return DatabaseHelper.getAll(db, WorkoutContract.WorkoutEntry.TABLE_NAME);
    }
}
