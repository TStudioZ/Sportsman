package com.tstudioz.sportsman.app.training;

import com.tstudioz.sportsman.app.time.TimeHelper;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Tomáš Zahálka on 10.5.14.
 */
public class Workout {

    public Workout(String datetime, long duration, float distance, int mood, Sport sport) {
        this.datetime = datetime;
        this.duration = duration;
        this.distance = distance;
        this.mood = mood;
        this.sport = sport;
        try {
            this.date = TimeHelper.getDateLocale(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public float getDistance() {
        return distance;
    }

    public int getMoodID() {
        return mood;
    }

    public Sport getSport() {
        return sport;
    }

    protected float distance;
    public void addDistance(float d) {
        distance += d;
    }

    public long getCalories() {
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

    public Calendar getDate() {
        return date;
    }

    private Calendar date;
    protected final String datetime;
    protected long duration;

    public void setMood(int mood) {
        this.mood = mood;
    }

    protected int mood;
    protected Sport sport;

    /**
     * Returns average speed in km/h.
     * @return average speed in km/h
     */
    public float getAvgSpeed() {
        return distance * 3.6f / (duration * 0.001f);
    }

}
