package com.tstudioz.sportsman.app;

import android.app.Activity;
import android.os.Bundle;

import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.training.Workout;
import com.tstudioz.sportsman.app.training.WorkoutWithWaypoints;


public class WorkoutFinishActivity extends Activity {

    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_finish);

        workout = ((SportsmanApp) getApplicationContext()).getActiveWorkout();
        saveWorkout();
        finish();
    }

    private void saveWorkout() {
        WorkoutDAO workoutDAO = new WorkoutDAO(getApplicationContext());
        long id = workoutDAO.add((WorkoutWithWaypoints) workout);
    }

}
