package com.tstudioz.sportsman.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.tstudioz.sportsman.app.activities.DialogHelper;
import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.activities.WorkoutDetailActivity;
import com.tstudioz.sportsman.app.database.WorkoutContract;
import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.adapters.WorkoutCursorAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class DiaryFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        DialogHelper.CommandListener {

    private WorkoutCursorAdapter adapter;
    private ListView workouts;
    private WorkoutDAO workoutDAO;
    private Spinner monthsSpinner;
    private Spinner yearsSpinner;

    private ArrayAdapter<CharSequence> monthsAdapter;
    private ArrayAdapter<Integer> yearsAdapter;

    private static final int YEAR_START = 2000;

    /* renews the cursor and adapter */
    private void refreshWorkouts() {
        adapter = new WorkoutCursorAdapter(getActivity(),
                workoutDAO.getWorkoutsByYearMonth(year, month));
        workouts.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshWorkouts();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutDAO = new WorkoutDAO(getActivity());
        spinnerSelectedListener = new SpinnerSelectedListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        workouts = (ListView) view.findViewById(R.id.workouts_list);
        workouts.setAdapter(adapter);
        workouts.setOnItemClickListener(this);
        registerForContextMenu(workouts);

        Calendar calendar = Calendar.getInstance();

        monthsSpinner = (Spinner) view.findViewById(R.id.spinner_months);
        monthsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.months, android.R.layout.simple_spinner_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthsSpinner.setAdapter(monthsAdapter);
        monthsSpinner.setOnItemSelectedListener(spinnerSelectedListener);
        month = calendar.get(Calendar.MONTH) + 1;
        monthsLastPosition = calendar.get(Calendar.MONTH);
        monthsSpinner.setSelection(monthsLastPosition);

        year = calendar.get(Calendar.YEAR);
        int numOfYears = year - YEAR_START + 1;
        List<Integer> years = new ArrayList<>(numOfYears);
        for (int y = year; y >= YEAR_START; y--) {
            years.add(y);
        }
        yearsSpinner = (Spinner) view.findViewById(R.id.spinner_years);
        yearsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsSpinner.setAdapter(yearsAdapter);
        yearsSpinner.setOnItemSelectedListener(spinnerSelectedListener);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.workout_item, menu);
    }

    /* current selected cursor */
    private Cursor currentCursor;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.workout_delete: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                currentCursor = (Cursor) workouts.getAdapter().getItem(info.position);
                command = DELETE_COMMAND;
                new DialogHelper(getActivity(), this)
                        .showYesNoDialog(getString(R.string.are_you_sure), DELETE_COMMAND);
                break;
            }
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) workouts.getAdapter().getItem(i);
        Intent intent = new Intent(getActivity(), WorkoutDetailActivity.class);
        intent.putExtra("workout_id", cursor.getLong(cursor.getColumnIndex(WorkoutContract.WorkoutEntry._ID)));
        getActivity().startActivity(intent);
    }

    private static final int DELETE_COMMAND = 1;
    int command;

    @Override
    public void onCommandSelected(int command) {
        if (command == DELETE_COMMAND) {
            workoutDAO.delete(currentCursor.getLong(currentCursor
                    .getColumnIndex(WorkoutContract.WorkoutEntry._ID)));
            refreshWorkouts();
        }
    }

    private int month;
    private int year;
    /* last positions for updating only when spinner selection changes */
    private int monthsLastPosition;
    private int yearsLastPosition;
    private SpinnerSelectedListener spinnerSelectedListener;
    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            boolean refresh = false;
            if (adapterView.getId() == monthsSpinner.getId() && monthsLastPosition != pos) {
                refresh = true;
                month = monthsSpinner.getSelectedItemPosition() + 1;
                monthsLastPosition = pos;
            }
            else if (adapterView.getId() == yearsSpinner.getId() && yearsLastPosition != pos) {
                refresh = true;
                year = (int) yearsSpinner.getSelectedItem();
                yearsLastPosition = pos;
            }
            if (refresh) {
                refreshWorkouts();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }
}
