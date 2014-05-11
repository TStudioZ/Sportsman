package com.tstudioz.sportsman.app.training;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tomáš Zahálka on 10.5.14.
 */
public class Waypoint {

    public double getSpeed() {
        return speed;
    }

    public double getDistance() {
        return distance;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    private final float speed;
    private final float distance;
    private final LatLng latLng;
    private final long duration;

    public String getDatetime() {
        return datetime;
    }

    public long getDuration() {
        return duration;
    }

    private final String datetime;

    public Waypoint(String datetime, long duration, float speed, float distance, LatLng latLng) {
        this.datetime = datetime;
        this.duration = duration;
        this.speed = speed;
        this.distance = distance;
        this.latLng = latLng;
    }
}
