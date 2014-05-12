package com.tstudioz.sportsman.app;

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

import com.tstudioz.sportsman.app.training.Sport;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class HomeFragment extends Fragment {

    private ArrayAdapter<Sport> adapter;
    private Button startWorkout;
    private Spinner sportsSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        startWorkout = (Button) view.findViewById(R.id.btn_start_workout);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                intent.putExtra("sport_id", ((Sport) sportsSpinner.getSelectedItem()).getId());
                getActivity().startActivity(intent);
            }
        });

        sportsSpinner = (Spinner) view.findViewById(R.id.sports_spinner);
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, Sport.getSports());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapter);

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

    /**
     * Saves the last selected sport to preferences.
     */
    private void saveSport() {
        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("sport_id", ((Sport) sportsSpinner.getSelectedItem()).getId());
        edit.apply();
    }
}
