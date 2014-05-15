package com.tstudioz.sportsman.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.activities.OnRefreshListener;
import com.tstudioz.sportsman.app.activities.WorkoutActivity;
import com.tstudioz.sportsman.app.activities.WorkoutEditActivity;
import com.tstudioz.sportsman.app.adapters.WorkoutCursorAdapter;
import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.time.TimeHelper;
import com.tstudioz.sportsman.app.training.Sport;
import com.tstudioz.sportsman.app.training.Workout;

import java.text.ParseException;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class HomeFragment extends Fragment implements OnRefreshListener {

    private ArrayAdapter<Sport> adapter;
    private Button btnStartWorkout;
    private Button btnAddWorkout;
    private Spinner sportsSpinner;

    private TableLayout lastWorkoutLayout;
    private TextView lastDistance;
    private TextView lastDuration;
    private TextView lastDate;
    private TextView lastSportName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnStartWorkout = (Button) view.findViewById(R.id.btn_start_workout);
        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                intent.putExtra("sport_id", ((Sport) sportsSpinner.getSelectedItem()).getId());
                getActivity().startActivity(intent);
            }
        });

        btnAddWorkout = (Button) view.findViewById(R.id.btn_add_workout);
        btnAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WorkoutEditActivity.class);
                getActivity().startActivity(intent);
            }
        });

        sportsSpinner = (Spinner) view.findViewById(R.id.sports_spinner);
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, Sport.getSports());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapter);

        lastWorkoutLayout = (TableLayout) view.findViewById(R.id.last_workout_layout);
        lastDistance = (TextView) view.findViewById(R.id.distance);
        lastDuration = (TextView) view.findViewById(R.id.duration);
        lastDate = (TextView) view.findViewById(R.id.date);
        lastSportName = (TextView) view.findViewById(R.id.sport_name);

        /* load the last selected sport from shared preferences */
        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int sportID = sharedPreferences.getInt("sport_id", 1);
        for (int i = 0; i < adapter.getCount(); i++) {
            if (sportID == adapter.getItem(i).getId()) {
                sportsSpinner.setSelection(i);
                break;
            }
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveSport();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveSport();
    }

    @Override
    public void onResume() {
        super.onResume();
        fillViewsLastWorkout();
    }

    /**
     * Saves the last selected sport to preferences.
     */
    private void saveSport() {
        if (sportsSpinner != null && sportsSpinner.getSelectedItem() != null) {
            SharedPreferences sharedPreferences
                    = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt("sport_id", ((Sport) sportsSpinner.getSelectedItem()).getId());
            edit.apply();
        }
    }

    /**
     * Gets last workout and displays it.
     */
    private void fillViewsLastWorkout() {
        Workout lastWorkout = new WorkoutDAO(getActivity()).getLastWorkout();
        if (lastWorkout == null) {
            lastWorkoutLayout.setVisibility(View.INVISIBLE); /* no workout found, hide the layout */
            return;
        }

        lastWorkoutLayout.setVisibility(View.VISIBLE);

        try {
            lastDate.setText(TimeHelper.convertDateToLocale(getActivity(), lastWorkout.getDatetime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lastDistance.setText(String.format("%.2f", lastWorkout.getDistance() * 0.001f) + WorkoutCursorAdapter.DISTANCE_UNITS);
        lastDuration.setText( TimeHelper.getTime(lastWorkout.getDuration()) );
        lastSportName.setText(lastWorkout.getSport().toString());
    }

    @Override
    public void onRefresh(Class<?> listener) {
        fillViewsLastWorkout();
    }
}
