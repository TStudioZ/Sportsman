package com.tstudioz.sportsman.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.training.WorkoutCursorAdapter;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class DiaryFragment extends Fragment {

    private WorkoutCursorAdapter adapter;
    private ListView workouts;
    private WorkoutDAO workoutDAO;

    @Override
    public void onDestroy() {
        super.onDestroy();
        workoutDAO.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutDAO = new WorkoutDAO(getActivity());
        workoutDAO.open();
        adapter = new WorkoutCursorAdapter(getActivity(), workoutDAO.getAll());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        workouts = (ListView) view.findViewById(R.id.workouts_list);
        workouts.setAdapter(adapter);
        return view;
    }
}
