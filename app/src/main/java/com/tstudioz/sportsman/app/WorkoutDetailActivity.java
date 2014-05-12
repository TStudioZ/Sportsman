package com.tstudioz.sportsman.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.time.TimeHelper;
import com.tstudioz.sportsman.app.training.Waypoint;
import com.tstudioz.sportsman.app.training.WorkoutCursorAdapter;
import com.tstudioz.sportsman.app.training.WorkoutWithWaypoints;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;


public class WorkoutDetailActivity extends Activity implements DialogHelper.CommandListener {

    private long workoutID;
    private WorkoutWithWaypoints workout;
    private WorkoutDAO workoutDAO;
    private TextView datetimeView;
    private TextView distanceView;
    private TextView durationView;
    private TextView sportNameView;
    private TextView caloriesView;
    private TextView avgSpeedView;
    private LinearLayout graphLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        workoutID = getIntent().getExtras().getLong("workout_id");
        workoutDAO = new WorkoutDAO(this);

        datetimeView = (TextView) findViewById(R.id.datetime);
        distanceView = (TextView) findViewById(R.id.distance);
        durationView = (TextView) findViewById(R.id.duration);
        sportNameView = (TextView) findViewById(R.id.sport_name);
        caloriesView = (TextView) findViewById(R.id.calories);
        avgSpeedView = (TextView) findViewById(R.id.avg_speed);
        graphLayout = (LinearLayout) findViewById(R.id.graph_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillViews();
    }

    private void fillViews() {
        workout = workoutDAO.getWorkout(workoutID);

        distanceView.setText(String.format("%.2f", workout.getDistance() * 0.001f) + WorkoutCursorAdapter.DISTANCE_UNITS);
        durationView.setText( TimeHelper.getTime(workout.getDuration()) );
        try {
            datetimeView.setText( TimeHelper.convertToLocale(this, workout.getDatetime()) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sportNameView.setText(workout.getSport().getNameID());
        caloriesView.setText( String.valueOf(workout.getCalories()) + WorkoutCursorAdapter.CALORIES_UNITS);
        avgSpeedView.setText( String.format("%.2f", workout.getAvgSpeed() * 3.6f) + WorkoutCursorAdapter.SPEED_UNITS);
        drawGraph();
    }

    private void drawGraph() {
        List<Waypoint> waypoints = workout.getWaypoints();
        if (waypoints.size() < 2) return;

        GraphViewData[] graphViewData = new GraphViewData[waypoints.size()];
        Iterator<Waypoint> iter = waypoints.iterator();
        Waypoint current;
        for (int i = 1; iter.hasNext(); i++) {
            current = iter.next();
            graphViewData[i - 1] = new GraphViewData(i, current.getSpeed());
        }
        GraphViewSeries series = new GraphViewSeries(graphViewData);
        GraphView graphView = new LineGraphView(this, getString(R.string.avg_speed_graph));
        graphView.addSeries(series);
        graphLayout.addView(graphView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.workout_detail, menu);
        return true;
    }

    private static final int DELETE_COMMAND = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("workout_id", workoutID);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_delete) {
            new DialogHelper(this, this).showYesNoDialog(getString(R.string.are_you_sure), DELETE_COMMAND);
            return true;
        }
        else if (id == R.id.action_edit) {
            Intent intent = new Intent(this, WorkoutEditActivity.class);
            intent.putExtra("workout_id", workoutID);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCommandSelected(int command) {
        if (command == DELETE_COMMAND) {
            workoutDAO.delete(workoutID);
            finish();
        }
    }
}
