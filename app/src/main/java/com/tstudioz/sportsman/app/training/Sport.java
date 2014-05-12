package com.tstudioz.sportsman.app.training;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tomáš Zahálka on 10.5.14.
 */
public class Sport {

    public int getId() {
        return id;
    }
    public long getCalories(float distanceMeters) {
        return (long) (distanceMeters * 0.001f * calories);
    }
    public int getNameID() {
        return nameID;
    }

    private final int id;
    private final int nameID;
    private final long calories;
    private final Context context;

    public Sport(Context context, int id, int nameID, long calories) {
        this.context = context;
        this.id = id;
        this.nameID = nameID;
        this.calories = calories;
    }

    @Override
    public String toString() {
        return context.getResources().getString(nameID);
    }

    /**
     * Returns a sport based on parameter.
     * @param sportId the id of the sport
     * @return sport
     */
    public static Sport getSport(int sportId) {
        return sports.get(sportId);
    }

    public static void addSport(Integer key, Sport sport) {
        sports.put(key, sport);
    }

    public static List<String> getSportsName(Resources resources) {
        List<String> names = new ArrayList<>(sports.size());
        Iterator<Sport> iterator = sports.values().iterator();
        while (iterator.hasNext()) {
            names.add( resources.getString(iterator.next().nameID) );
        }
        return names;
    }

    public static List<Sport> getSports() {
        return new ArrayList<>(sports.values());
    }

    static {
        sports = new HashMap<>();
    }

    private static HashMap<Integer, Sport> sports;
}
