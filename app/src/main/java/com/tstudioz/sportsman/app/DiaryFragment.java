package com.tstudioz.sportsman.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.ListView;

import com.tstudioz.sportsman.app.database.WorkoutContract;
import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.training.WorkoutCursorAdapter;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class DiaryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private WorkoutCursorAdapter adapter;
    private ListView workouts;
    private WorkoutDAO workoutDAO;

    @Override
    public void onResume() {
        super.onResume();
        adapter = new WorkoutCursorAdapter(getActivity(), workoutDAO.getAll());
        workouts.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutDAO = new WorkoutDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        workouts = (ListView) view.findViewById(R.id.workouts_list);
        workouts.setAdapter(adapter);
        workouts.setOnItemClickListener(this);
        registerForContextMenu(workouts);
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
                showYesNoDialog(getString(R.string.are_you_sure));
                break;
            }
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) workouts.getAdapter().getItem(i);
        Intent intent = new Intent(getActivity(), MapActivity.class);
        intent.putExtra("workout_id", cursor.getLong(cursor.getColumnIndex(WorkoutContract.WorkoutEntry._ID)));
        getActivity().startActivity(intent);
    }

    private static final int DELETE_COMMAND = 1;
    int command;
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    if (command == DELETE_COMMAND) {
                        workoutDAO.delete(currentCursor.getLong(currentCursor
                                .getColumnIndex(WorkoutContract.WorkoutEntry._ID)));
                        adapter.changeCursor(workoutDAO.getAll());
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    private void showYesNoDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }
}
