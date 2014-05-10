package com.tstudioz.sportsman.app.training;

/**
 * Created by Tomáš Zahálka on 10.5.14.
 */
public class Workout {

    public Workout(long id, double distance, long cals, int mood, int weather, Sport sport) {
        this.id = id;
        this.distance = distance;
        this.cals = cals;
        this.mood = mood;
        this.weather = weather;
        this.sport = sport;
    }

    public double getDistance() {
        return distance;
    }

    public long getCals() {
        return cals;
    }

    public int getMood() {
        return mood;
    }

    public int getWeather() {
        return weather;
    }

    public Sport getSport() {
        return sport;
    }

    public long getId() {
        return id;
    }

    private long id;
    private double distance;
    private long cals;

    private int mood;
    private int weather;
    private Sport sport;

}
