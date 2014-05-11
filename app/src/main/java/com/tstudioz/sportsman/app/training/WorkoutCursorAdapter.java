package com.tstudioz.sportsman.app.training;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.database.WorkoutContract;
import com.tstudioz.sportsman.app.time.TimeHelper;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class WorkoutCursorAdapter extends CursorAdapter {

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

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView datetime = (TextView) view.findViewById(R.id.datetime);
        TextView distance = (TextView) view.findViewById(R.id.distance);
        TextView duration = (TextView) view.findViewById(R.id.duration);
        distance.setText( Double.toString( cursor.getDouble(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DISTANCE ))) );
        datetime.setText( cursor.getString(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DATETIME)) );
        duration.setText( TimeHelper.getTime(cursor.getLong(cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DURATION))) );
    }

}
