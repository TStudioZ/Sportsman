package com.tstudioz.sportsman.app.training;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class WorkoutWithWaypoints extends Workout {

    public WorkoutWithWaypoints(String datetime, Sport sport) {
        super(datetime, 0, 0.0f, 0, sport);
        this.waypoints = new ArrayList<>();
    }

    public WorkoutWithWaypoints(Workout workout, List<Waypoint> waypoints) {
        super(workout.datetime, workout.duration, workout.distance, workout.mood, workout.sport);
        this.waypoints = waypoints;
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
        averageWaypoints();
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    private List<Waypoint> waypoints;

    private static final int MAX_WAYPOINTS = 100;
    private static final int MAX_WAYPOINTS_REALLY = MAX_WAYPOINTS * 2;

    /* discards every second waypoint */
    private void averageWaypoints() {
        if (waypoints.size() >= MAX_WAYPOINTS_REALLY) {
            List<Waypoint> averagedWaypoints = new ArrayList<>(MAX_WAYPOINTS);
            for (int i = 0; i < waypoints.size(); i += 2) {
                averagedWaypoints.add(waypoints.get(i));
            }
            waypoints = averagedWaypoints;
        }
    }
}
