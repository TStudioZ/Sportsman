package com.tstudioz.sportsman.app;

import android.app.Application;

import com.tstudioz.sportsman.app.training.Workout;

/**
 * Created by Tomáš Zahálka on 11. 5. 2014.
 */
public class SportsmanApp extends Application {
    private WorkoutService workoutService;

    public WorkoutActivity getWorkoutActivity() {
        return workoutActivity;
    }

    public void setWorkoutActivity(WorkoutActivity workoutActivity) {
        this.workoutActivity = workoutActivity;
    }

    public WorkoutService getWorkoutService() {
        return workoutService;
    }

    public void setWorkoutService(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    private WorkoutActivity workoutActivity;

    public Workout getActiveWorkout() {
        return activeWorkout;
    }

    public void setActiveWorkout(Workout activeWorkout) {
        this.activeWorkout = activeWorkout;
    }

    private Workout activeWorkout;
}
