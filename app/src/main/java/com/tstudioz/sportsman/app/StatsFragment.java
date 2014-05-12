package com.tstudioz.sportsman.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.training.Sport;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class StatsFragment extends Fragment {

    private Button btnGraphs;

    @Override
    public void onResume() {
        super.onResume();

        WorkoutDAO workoutDAO = new WorkoutDAO(getActivity());
        int numOfWorkouts = workoutDAO.getNumOfWorkouts(Sport.getSports().get(0).getId());
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
        return view;
    }

}
