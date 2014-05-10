package com.tstudioz.sportsman.app.training;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tomáš Zahálka on 10.5.14.
 */
public class Waypoint {

    public long getWorkoutID() {
        return workoutID;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDistance() {
        return distance;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public long getId() {
        return id;
    }

    private long id;
    private long workoutID;
    private double speed;
    private double distance;
    private LatLng latLng;

    public Waypoint(long id, long workoutID, double speed, double distance, LatLng latLng) {
        this.id = id;
        this.workoutID = workoutID;
        this.speed = speed;
        this.distance = distance;
        this.latLng = latLng;
    }
}
