package com.tstudioz.sportsman.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tstudioz.sportsman.app.training.Sport;


public class StatsActivity extends Activity {

    private ArrayAdapter<Sport> sportsAdapter;
    private ArrayAdapter<CharSequence> graphsAdapter;
    private Spinner sportsSpinner;
    private Spinner graphsSpinner;
    private LinearLayout graphLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        spinnerSelectedListener = new SpinnerSelectedListener();

        sportsSpinner = (Spinner) findViewById(R.id.spinner_sports);
        sportsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Sport.getSports());
        sportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(sportsAdapter);
        sportsSpinner.setOnItemSelectedListener(spinnerSelectedListener);

        graphsSpinner = (Spinner) findViewById(R.id.spinner_graphs);
        graphsAdapter = ArrayAdapter.createFromResource(this, R.array.graphs, android.R.layout.simple_spinner_item);
        graphsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphsSpinner.setOnItemSelectedListener(spinnerSelectedListener);
        graphsSpinner.setAdapter(graphsAdapter);

        graphLayout = (LinearLayout) findViewById(R.id.graph_layout);
    }

    private SpinnerSelectedListener spinnerSelectedListener;
    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            drawGraph();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }

    private void drawGraph() {
        int graphIndex = graphsSpinner.getSelectedItemPosition();
    }

}
