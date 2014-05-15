package com.tstudioz.sportsman.app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.database.WorkoutContract;
import com.tstudioz.sportsman.app.time.TimeHelper;
import com.tstudioz.sportsman.app.training.Mood;
import com.tstudioz.sportsman.app.training.Sport;

import java.text.ParseException;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class WorkoutCursorAdapter extends CursorAdapter {

    public static final String DISTANCE_UNITS = " km";
    public static final String CALORIES_UNITS = " kcal";
    public static final String SPEED_UNITS = " km/h";

    /**
     * Creates an cursor adapter, which reads workouts from database
     * and converts them to views.
     * @param context context
     * @param cursor cursor
     */
    public WorkoutCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return layoutInflater.inflate(R.layout.workout_row, viewGroup, false);
    }

    /**
     * Populates the row view with cursor values.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView datetimeView = (TextView) view.findViewById(R.id.datetime);
        TextView distanceView = (TextView) view.findViewById(R.id.distance);
        TextView durationView = (TextView) view.findViewById(R.id.duration);
        TextView sportNameView = (TextView) view.findViewById(R.id.sport_name);
        TextView caloriesView = (TextView) view.findViewById(R.id.calories);
        ImageView moodView = (ImageView) view.findViewById(R.id.mood);

        float distance = cursor.getFloat(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DISTANCE ));
        long duration = cursor.getLong(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DURATION));
        String datetime = cursor.getString(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME));
        Sport sport = Sport.getSport( cursor.getInt(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_SPORT_ID)) );
        int moodID = cursor.getInt(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_MOOD_ID));

        distanceView.setText(String.format("%.2f", distance * 0.001f) + DISTANCE_UNITS);
        durationView.setText( TimeHelper.getTime(duration) );
        try {
            datetimeView.setText( TimeHelper.convertDateToLocale(context, datetime) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sportNameView.setText(sport.getNameID());
        caloriesView.setText( String.valueOf(sport.getCalories(distance)) + CALORIES_UNITS );
        moodView.setImageResource(Mood.getDrawableID(moodID));
    }

}
