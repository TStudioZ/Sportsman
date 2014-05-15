package com.tstudioz.sportsman.app.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.time.TimeHelper;
import com.tstudioz.sportsman.app.training.Sport;
import com.tstudioz.sportsman.app.training.Workout;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class StatsActivity extends Activity {

    private ArrayAdapter<Sport> sportsAdapter;
    private ArrayAdapter<CharSequence> graphsAdapter;
    private Spinner sportsSpinner;
    private Spinner graphsSpinner;

    private static final int NUM_OF_GRAPH_LAYOUTS = 2;
    private LinearLayout[] graphLayouts;
    private WorkoutDAO workoutDAO;

    private static final int WORKOUTS_ONE = 7;
    private static final int WORKOUTS_TWO = 30;
    private List<Workout> workouts7;
    private List<Workout> workouts30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        spinnerSelectedListener = new SpinnerSelectedListener();

        List<Sport> sports = Sport.getSports();
        sports.add(0, new Sport(this, 0, R.string.all, 0));
        sportsSpinner = (Spinner) findViewById(R.id.spinner_sports);
        sportsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sports);
        sportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(sportsAdapter);
        sportsSpinner.setOnItemSelectedListener(spinnerSelectedListener);

        graphsSpinner = (Spinner) findViewById(R.id.spinner_graphs);
        graphsAdapter = ArrayAdapter.createFromResource(this, R.array.graphs, android.R.layout.simple_spinner_item);
        graphsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphsSpinner.setAdapter(graphsAdapter);
        graphsSpinner.setOnItemSelectedListener(spinnerSelectedListener);

        graphLayouts = new LinearLayout[NUM_OF_GRAPH_LAYOUTS];
        graphLayouts[0] = (LinearLayout) findViewById(R.id.graph_layout1);
        graphLayouts[1] = (LinearLayout) findViewById(R.id.graph_layout2);

        workoutDAO = new WorkoutDAO(this);
    }

    private SpinnerSelectedListener spinnerSelectedListener;
    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getId() == sportsSpinner.getId()) {
                int sportID = ((Sport) sportsSpinner.getSelectedItem()).getId();
                if ( sportID == 0 ) {
                    workouts7 = workoutDAO.getWorkoutsLastDays(WORKOUTS_ONE);
                    workouts30 = workoutDAO.getWorkoutsLastDays(WORKOUTS_TWO);
                }
                else {
                    workouts7 = workoutDAO.getWorkoutsLastDays(WORKOUTS_ONE, sportID);
                    workouts30 = workoutDAO.getWorkoutsLastDays(WORKOUTS_TWO, sportID);
                }
            }
            drawGraph();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }

    private void drawGraph() {
        GraphView graphView1 = null, graphView2 = null;
        for (int i = 0; i < graphLayouts.length; i++) {
            graphLayouts[i].removeAllViews();
        }
        int graphIndex = graphsSpinner.getSelectedItemPosition();
        switch (graphIndex) {
            case 0: { // distance
                graphView1 = drawDistance(workouts7, WORKOUTS_ONE, getString(R.string.last_7_days));
                graphView2 = drawDistance(workouts30, WORKOUTS_TWO, getString(R.string.last_30_days));
                break;
            }
            case 1: { // duration
                graphView1 = drawDuration(workouts7, WORKOUTS_ONE, getString(R.string.last_7_days));
                graphView2 = drawDuration(workouts30, WORKOUTS_TWO, getString(R.string.last_30_days));
                break;
            }
            case 2: { // calories
                graphView1 = drawCalories(workouts7, WORKOUTS_ONE, getString(R.string.last_7_days));
                graphView2 = drawCalories(workouts30, WORKOUTS_TWO, getString(R.string.last_30_days));
                break;
            }
            case 3: { // average speed
                graphView1 = drawAverageSpeed(workouts7, WORKOUTS_ONE, getString(R.string.last_7_days));
                graphView2 = drawAverageSpeed(workouts30, WORKOUTS_TWO, getString(R.string.last_30_days));
                break;
            }
        }
        if (graphView1 != null)
            graphLayouts[0].addView(graphView1);
        if (graphView2 != null)
            graphLayouts[1].addView(graphView2);
    }

    private GraphView drawDistance(List<Workout> workouts, int numOfDays, String label) {
        return getGraph(new GraphDataProvider() {
            @Override
            public float getValue(Workout workout) {
                return workout.getDistance() * 0.001f;
            }

            @Override
            public GraphView getGraphView(String label) {
                return new BarGraphView(StatsActivity.this, label);
            }

            @Override
            public int getColor() {
                return Color.rgb(0x99, 0xcc, 0x00);
            }
        }, workouts, numOfDays, label);
    }

    private GraphView drawDuration(List<Workout> workouts, int numOfDays, String label) {
        return getGraph(new GraphDataProvider() {
            @Override
            public float getValue(Workout workout) {
                return workout.getDuration() * 0.001f;
            }

            @Override
            public GraphView getGraphView(String label) {
                return new LineGraphView(StatsActivity.this, label);
            }

            @Override
            public int getColor() {
                return Color.rgb(0x33, 0xb5, 0xe5);
            }
        }, workouts, numOfDays, label);
    }

    private GraphView drawCalories(List<Workout> workouts, int numOfDays, String label) {
        return getGraph(new GraphDataProvider() {
            @Override
            public float getValue(Workout workout) {
                return workout.getCalories();
            }

            @Override
            public GraphView getGraphView(String label) {
                return new BarGraphView(StatsActivity.this, label);
            }

            @Override
            public int getColor() {
                return Color.rgb(0xff, 0x44, 0x44);
            }
        }, workouts, numOfDays, label);
    }

    private GraphView drawAverageSpeed(List<Workout> workouts, int numOfDays, String label) {
        return getGraph(new GraphDataProvider() {
            @Override
            public float getValue(Workout workout) {
                return workout.getAvgSpeed();
            }

            @Override
            public GraphView getGraphView(String label) {
                return new LineGraphView(StatsActivity.this, label);
            }

            @Override
            public int getColor() {
                return Color.rgb(0xff, 0xbb, 0x33);
            }
        }, workouts, numOfDays, label);
    }

    private GraphView getGraph(GraphDataProvider provider, List<Workout> workouts, int numOfDays, String label) {
        if (workouts.size() == 0) {
            return null;
        }
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, -numOfDays + 1);
        Calendar startDate = (Calendar) date.clone();
        GraphViewData[] graphViewData = new GraphViewData[numOfDays];
        Iterator<Workout> iter = workouts.iterator();
        Workout currentWorkout = iter.next();
        float max = provider.getValue(currentWorkout), value;
        for (int i = 0; i < numOfDays; i++, date.add(Calendar.DATE, 1)) {
            if ( currentWorkout != null ) {
                value = 0.0f;
                while ( currentWorkout != null && TimeHelper.equalDays(date, currentWorkout.getDate()) ) {
                    value += provider.getValue(currentWorkout);
                    if (iter.hasNext())
                        currentWorkout = iter.next();
                    else
                        currentWorkout = null;
                }
                if (value > max)
                    max = value;
                graphViewData[i] = new GraphViewData(i, value);
            }
            else
                graphViewData[i] = new GraphViewData(i, 0.0f);
        }
        GraphView graphView = provider.getGraphView(label);
        GraphViewSeries.GraphViewSeriesStyle seriesStyle
                = new GraphViewSeries.GraphViewSeriesStyle(provider.getColor(), 3);
        graphView.setManualYAxisBounds(max, 0.0f);

        date.add(Calendar.DATE, -1);
        graphView.setHorizontalLabels(new String[]{ TimeHelper.getDateLocale(this, startDate),
                TimeHelper.getDateLocale(this, date) });
        graphView.getGraphViewStyle().setNumHorizontalLabels(0);
        GraphViewSeries series = new GraphViewSeries("", seriesStyle, graphViewData);
        graphView.addSeries(series);
        return graphView;
    }

    private abstract class GraphDataProvider {
        public abstract float getValue(Workout workout);
        public abstract GraphView getGraphView(String label);
        public abstract int getColor();
    }

}
