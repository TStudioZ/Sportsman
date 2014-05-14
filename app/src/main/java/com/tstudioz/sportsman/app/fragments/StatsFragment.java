package com.tstudioz.sportsman.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.activities.StatsActivity;
import com.tstudioz.sportsman.app.database.WorkoutDAO;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class StatsFragment extends Fragment {

    private Button btnGraphs;
    private LinearLayout statsLayout;

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
        statsLayout = (LinearLayout) view.findViewById(R.id.stats_layout);
        return view;
    }

    private void refreshViewsStats() {
        statsLayout.removeAllViews();

        WorkoutDAO workoutDAO = new WorkoutDAO(getActivity());
        int numOfWorkouts = workoutDAO.getNumOfWorkouts();

        statsLayout.addView(getStatView(R.string.total_workouts, String.valueOf(numOfWorkouts)));
    }

    private LinearLayout getStatView(int textID, String value) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(textID);
        linearLayout.addView(textView);

        textView = new TextView(getActivity());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(value);
        linearLayout.addView(textView);

        return linearLayout;
    }

}
