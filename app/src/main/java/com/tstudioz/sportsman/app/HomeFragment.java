package com.tstudioz.sportsman.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tstudioz.sportsman.app.training.WorkoutCursorAdapter;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class HomeFragment extends Fragment {

    private WorkoutCursorAdapter adapter;
    private Button startWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        startWorkout = (Button) view.findViewById(R.id.btn_start_workout);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                intent.putExtra("sport_id", 0);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}
