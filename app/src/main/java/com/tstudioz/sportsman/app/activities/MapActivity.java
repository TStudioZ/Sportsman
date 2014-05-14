package com.tstudioz.sportsman.app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.database.WaypointDAO;
import com.tstudioz.sportsman.app.training.Waypoint;

import java.util.Iterator;
import java.util.List;

public class MapActivity extends FragmentActivity {

    private GoogleMap map;
    private List<Waypoint> waypoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();
        if (getIntent().getExtras() != null) {
            long workoutID = getIntent().getExtras().getLong("workout_id");
            waypoints = new WaypointDAO(this).getAllWaypointsByWorkoutID(workoutID);
        }
        showWaypoints();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
    }

    private void showWaypoints() {

        /* if less than 1 waypoint, don\'t show anything */
        if (waypoints.size() < 1) return;

        PolylineOptions polylineOptions = new PolylineOptions();
        Iterator<Waypoint> iterator = waypoints.iterator();
        Waypoint currentWaypoint;
        currentWaypoint = iterator.next();
        polylineOptions.add(currentWaypoint.getLatLng());

        /* start waypoint */
        map.addMarker(new MarkerOptions()
                .position(currentWaypoint.getLatLng())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .flat(true));

        /* goal waypoint */
        map.addMarker(new MarkerOptions()
                .position(waypoints.get(waypoints.size() - 1).getLatLng())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .flat(true));

        double mostWest = currentWaypoint.getLatLng().longitude,
                mostNorth = currentWaypoint.getLatLng().latitude,
                mostEast = currentWaypoint.getLatLng().longitude,
                mostSouth = currentWaypoint.getLatLng().latitude;
        while (iterator.hasNext()) {
            currentWaypoint = iterator.next();
            polylineOptions.add(currentWaypoint.getLatLng()).width(4.0f).color(Color.CYAN);

            if (currentWaypoint.getLatLng().longitude < mostWest)
                mostWest = currentWaypoint.getLatLng().longitude;
            else if (currentWaypoint.getLatLng().longitude > mostEast)
                mostEast = currentWaypoint.getLatLng().longitude;
            if (currentWaypoint.getLatLng().latitude > mostNorth)
                mostNorth = currentWaypoint.getLatLng().latitude;
            else if (currentWaypoint.getLatLng().latitude < mostSouth)
                mostSouth = currentWaypoint.getLatLng().latitude;
        }

        if (polylineOptions.getPoints().size() > 1)
            map.addPolyline(polylineOptions);

        LatLngBounds bounds = new LatLngBounds(
                new LatLng(mostSouth, mostWest),
                new LatLng(mostNorth, mostEast));

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels, 100));
    }
}
