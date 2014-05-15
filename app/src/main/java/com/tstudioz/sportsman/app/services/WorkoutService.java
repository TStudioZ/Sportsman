package com.tstudioz.sportsman.app.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.tstudioz.sportsman.app.SportsmanApp;
import com.tstudioz.sportsman.app.time.TimeHelper;
import com.tstudioz.sportsman.app.training.Sport;
import com.tstudioz.sportsman.app.training.Waypoint;
import com.tstudioz.sportsman.app.training.WorkoutWithWaypoints;

import java.util.Timer;
import java.util.TimerTask;

public class WorkoutService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,LocationListener {

    private LocationClient locationClient;
    private LocationRequest locationRequest;

    /* interval for receiving location updates */
    private static final int UPDATE_INTERVAL = 2000;

    public static final String INTENT_DISTANCE_UPDATE = "distance_update";
    public static final String INTENT_TIME_UPDATE = "time_update";

    @Override
    public void onCreate() {
        super.onCreate();
        ((SportsmanApp) getApplicationContext()).setWorkoutService(this);

        locationRequest = locationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(UPDATE_INTERVAL);

        locationClient = new LocationClient(this, this, ((SportsmanApp) getApplicationContext()).getWorkoutActivity());
        locationClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (sport == null) {
            sport = Sport.getSport( intent.getIntExtra("sport_id", 0) );
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient.isConnected()) {
            locationClient.removeLocationUpdates(this);
            locationClient.disconnect();
        }
    }

    /* not used */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onDisconnected() {}

    /**
     * Pauses the workout - stops sending broadcasts and adding distance.
     */
    public void pauseWorkout() {
        locationClient.removeLocationUpdates(this);
        paused = true;
        timer.cancel();
        timer = null;
        lastWaypoint = lastLocation = null;
    }

    /**
     * Resumes the workout - starts sending broadcasts and adding distance.
     */
    public void resumeWorkout() {
        if (workout == null) {
            workout = new WorkoutWithWaypoints(TimeHelper.getCurrentTimestamp(), sport);
        }
        locationClient.requestLocationUpdates(locationRequest, this);
        lastTimeMilisecs = System.currentTimeMillis();
        paused = false;
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new UpdateTime(), 0, 1000);
        }
    }

    /**
     * Stops the workout.
     */
    public void stopWorkout() {
        workout.setDuration(totalTimeMilisecs + (System.currentTimeMillis() - lastTimeMilisecs));
        if (lastWaypoint != null)
            workout.addWaypoint(new Waypoint(TimeHelper.getCurrentTimestamp(),
                    totalTimeMilisecs,
                    speed, workout.getDistance(),
                    new LatLng(lastWaypoint.getLatitude(), lastWaypoint.getLongitude())));
        ((SportsmanApp) getApplicationContext()).setActiveWorkout(workout);
    }

    /* workout in progress */
    private WorkoutWithWaypoints workout;
    /* is workout paused */
    private boolean paused = true;
    /* stores the total workout time */
    private long totalTimeMilisecs = 0;
    private long lastTimeMilisecs;
    private long lastIntervalMilisecs;
    private float speed;
    private Sport sport;

    private Timer timer;
    private class UpdateTime extends TimerTask {
        @Override
        public void run() {
            updateTime();
            Intent intent = new Intent(INTENT_TIME_UPDATE);
            intent.putExtra("time", TimeHelper.getTime(totalTimeMilisecs));
            LocalBroadcastManager.getInstance(WorkoutService.this).sendBroadcast(intent);
        }
    }

    /* counts total time */
    private void updateTime() {
        lastIntervalMilisecs = System.currentTimeMillis() - lastTimeMilisecs;
        lastTimeMilisecs = System.currentTimeMillis();
        totalTimeMilisecs += lastIntervalMilisecs;
    }

    /**
     * Updates workout info and sends appropriate broadcast.
     * @param location current location
     */
    private void updateWorkout(Location location) {
        if (!paused) {
            updateTime();
            if (lastLocation != null) {
                float distance[] = new float[1];
                Location.distanceBetween(lastLocation.getLatitude(), lastLocation.getLongitude(),
                        location.getLatitude(), location.getLongitude(), distance); // determine distance between last and new location
                speed = distance[0] * 3.6f / (lastIntervalMilisecs * 0.001f); // determine speed based on distance and elapsed time

                workout.addDistance(distance[0]);
                Intent intent = new Intent(INTENT_DISTANCE_UPDATE);
                intent.putExtra("distance", workout.getDistance());
                intent.putExtra("speed", speed);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent); // send distance update broadcast

                if (lastWaypoint != null)
                    Location.distanceBetween(lastWaypoint.getLatitude(), lastWaypoint.getLongitude(),
                            location.getLatitude(), location.getLongitude(), distance);

                if (distance[0] >= MIN_WAYPOINT_DISTANCE || lastWaypoint == null) { // add new waypoint to the workout if we passed the distance
                    workout.addWaypoint(new Waypoint(TimeHelper.getCurrentTimestamp(),
                            totalTimeMilisecs,
                            speed, workout.getDistance(),
                            new LatLng(location.getLatitude(), location.getLongitude())));
                    lastWaypoint = location;
                }
            }
        }
        lastLocation = location; // store updated location for the next update
    }

    /* min distance to add waypoint */
    private static final float MIN_WAYPOINT_DISTANCE = 10.0f;
    /* previous updated location */
    private Location lastLocation = null;
    /* location of the last recorded waypoint */
    private Location lastWaypoint = null;
    @Override

    public void onLocationChanged(Location location) {
        updateWorkout(location);
    }
}
