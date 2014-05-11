package com.tstudioz.sportsman.app.training;

/**
 * Created by Tomáš Zahálka on 10.5.14.
 */
public class Sport {

    public int getId() {
        return id;
    }

    private int id;

    public Sport(int id) {
        this.id = id;
    }

    public long getCalories(float distanceMeters) {
        return 0l;
    }

    /**
     * Returns new instance of Sport based on parameter.
     * @param sportId the id of the sport
     * @return instance of Sport or it\'s subclasses
     */
    public static Sport newInstance(int sportId) {
        return new Sport(0);
    }
}
