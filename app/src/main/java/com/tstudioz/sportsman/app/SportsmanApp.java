package com.tstudioz.sportsman.app;

import android.app.Application;
import android.content.res.AssetManager;

import com.tstudioz.sportsman.app.activities.WorkoutActivity;
import com.tstudioz.sportsman.app.services.WorkoutService;
import com.tstudioz.sportsman.app.training.Sport;
import com.tstudioz.sportsman.app.training.Workout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    @Override
    public void onCreate() {
        super.onCreate();
        loadSports();
    }

    /**
     * Loads all sports with id and number of calories from assets file.
     */
    private void loadSports() {
        String line;
        String[] splitted;
        int id, nameID;
        AssetManager assetsManager = getAssets();
        try {
            InputStream is = assetsManager.open("sports.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while ( (line = br.readLine()) != null ) {
                splitted = line.split(" ");
                id = Integer.parseInt(splitted[0]);
                nameID = getResources().getIdentifier(splitted[1], "string", getPackageName());
                Sport.addSport(id, new Sport(getApplicationContext(), id, nameID, Integer.parseInt(splitted[2])));
            }

            is.close();
            isr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
