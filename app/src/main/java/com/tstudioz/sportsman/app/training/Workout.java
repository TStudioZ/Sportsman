package com.tstudioz.sportsman.app.training;

/**
 * Created by Tomáš Zahálka on 10.5.14.
 */
public class Workout {

    public Workout(String datetime, long duration, float distance, int mood, int weather, Sport sport) {
        this.datetime = datetime;
        this.duration = duration;
        this.distance = distance;
        this.mood = mood;
        this.weather = weather;
        this.sport = sport;
    }

    public float getDistance() {
        return distance;
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

    private float distance;
    public void addDistance(float d) {
        distance += d;
    }

    public long getCals() {
        return sport.getCalories(distance);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getDatetime() {
        return datetime;
    }

    private String datetime;
    private long duration;
    private int mood;
    private int weather;
    private Sport sport;

    public float getAvgSpeed() {
        return distance / (duration * 0.001f);
    }

}
