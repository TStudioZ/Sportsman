package com.tstudioz.sportsman.app.training;

import java.util.List;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class WorkoutWithWaypoints extends Workout {
    public WorkoutWithWaypoints(long id, double distance, long cals, int mood,
                                int weather, Sport sport, List<Waypoint> waypoints) {
        super(id, distance, cals, mood, weather, sport);
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
