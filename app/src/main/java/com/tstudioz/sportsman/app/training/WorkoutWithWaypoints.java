package com.tstudioz.sportsman.app.training;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class WorkoutWithWaypoints extends Workout {

    public WorkoutWithWaypoints(String datetime, Sport sport) {
        super(datetime, 0, 0.0f, 0, sport);
        waypoints = new ArrayList<>();
    }

    public WorkoutWithWaypoints(String duration, long time, float distance, int mood,
                                Sport sport, List<Waypoint> waypoints) {
        super(duration, time, distance, mood, sport);
        this.waypoints = waypoints;
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    private List<Waypoint> waypoints;
}
