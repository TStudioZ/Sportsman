package com.tstudioz.sportsman.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.activities.OnRefreshListener;
import com.tstudioz.sportsman.app.activities.StatsActivity;
import com.tstudioz.sportsman.app.adapters.WorkoutCursorAdapter;
import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.training.Sport;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class StatsFragment extends Fragment implements OnRefreshListener {

    private Button btnGraphs;
    private TableLayout statsLayout;

    @Override
    public void onResume() {
        super.onResume();
        refreshViewsStats();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        btnGraphs = (Button) view.findViewById(R.id.btn_graphs);
        btnGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StatsActivity.class);
                startActivity(intent);
            }
        });
        statsLayout = (TableLayout) view.findViewById(R.id.stats_layout);
        return view;
    }

    private void refreshViewsStats() {
        statsLayout.removeAllViews();

        WorkoutDAO workoutDAO = new WorkoutDAO(getActivity());
        int numOfWorkouts = workoutDAO.getNumOfWorkouts();

        statsLayout.addView(getStatView(getString(R.string.total_workouts), String.valueOf(numOfWorkouts)));
        List<Sport> sports = Sport.getSports();
        Iterator<Sport> iter = sports.iterator();
        Sport currentSport;
        while (iter.hasNext()) {
            currentSport = iter.next();
            numOfWorkouts = workoutDAO.getNumOfWorkouts(currentSport.getId());
            statsLayout.addView(getStatView(getString(R.string.num_of_workouts)
                    + " - " + currentSport + ": ", String.valueOf(numOfWorkouts)));
        }

        statsLayout.addView(getStatView("", ""));

        statsLayout.addView(getStatView(getString(R.string.total_distance),
                String.format("%.2f", workoutDAO.getTotalDistance() * 0.001f)
                        + WorkoutCursorAdapter.DISTANCE_UNITS));

        long totalCalories = 0;
        float distance;
        iter = sports.iterator();
        while (iter.hasNext()) {
            currentSport = iter.next();
            distance = workoutDAO.getTotalDistance(currentSport.getId());
            totalCalories += currentSport.getCalories(distance);
            statsLayout.addView(getStatView(currentSport + ": ",
                    String.format("%.2f", distance * 0.001f)
                            + WorkoutCursorAdapter.DISTANCE_UNITS));
        }

        statsLayout.addView(getStatView("", ""));

        statsLayout.addView(getStatView(getString(R.string.total_calories), String.valueOf(totalCalories)));
    }

    private LinearLayout getStatView(String text, String value) {
        TableRow row = new TableRow(getActivity());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(getActivity());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textView.setText(text);
        row.addView(textView);

        textView = new TextView(getActivity());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textView.setText(value);
        row.addView(textView);

        return row;
    }

    private OnRefreshListener refreshListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            refreshListener = (OnRefreshListener) activity;
        } catch (ClassCastException e) {
            Log.d(StatsFragment.class.toString(),
                    activity.getClass().toString() + " must implement "
                            + OnRefreshListener.class.toString() + ".");
        }
    }

    @Override
    public void onRefresh(Class<?> listener) {
        //refreshViewsStats();
    }
}
