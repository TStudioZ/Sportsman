package com.tstudioz.sportsman.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.components.TimeInput;
import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.time.TimeHelper;
import com.tstudioz.sportsman.app.training.Sport;
import com.tstudioz.sportsman.app.training.Workout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class WorkoutEditActivity extends Activity {

    private Spinner sportsSpinner;
    private EditText editTextDistance;
    private TimeInput timeInputDuration;
    private TimeInput timeInputStart;
    private DatePicker datePicker;
    private Button btnSave;

    private long workoutID;
    private Workout workout;
    private WorkoutDAO workoutDAO;
    private ArrayAdapter<Sport> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_edit);

        workoutDAO = new WorkoutDAO(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workoutID = extras.getLong("workout_id");
            workout = workoutDAO.getWorkout(workoutID);
        }

        sportsSpinner = (Spinner) findViewById(R.id.edit_sport);
        timeInputDuration = (TimeInput) findViewById(R.id.edit_duration);
        timeInputStart = (TimeInput) findViewById(R.id.edit_start_time);
        datePicker = (DatePicker) findViewById(R.id.edit_start_date);
        editTextDistance = (EditText) findViewById(R.id.edit_distance);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Sport.getSports());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapter);
        editTextDistance.setFilters(new InputFilter[]{new DistanceFilter()});

        btnSave = (Button) findViewById(R.id.edit_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWorkout();
            }
        });

        if (workout != null)
            fillViews(workout);
    }

    /**
     * Fills views with information in the workout.
     * @param workout workout with information
     */
    private void fillViews(Workout workout) {
        int sportID = workout.getSport().getId();
        for (int i = 0; i < adapter.getCount(); i++) {
            if ( sportID == adapter.getItem(i).getId()) {
                sportsSpinner.setSelection(i);
                break;
            }
        }

        timeInputDuration.setTime(workout.getDuration());
        editTextDistance.setText(String.valueOf(workout.getDistance() * 0.001f));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(workout.getDatetime());
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            timeInputStart.setHours(calendar.get(Calendar.HOUR_OF_DAY));
            timeInputStart.setMinutes(calendar.get(Calendar.MINUTE));
            timeInputStart.setMinutes(calendar.get(Calendar.SECOND));
            datePicker.updateDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void saveWorkout() {
        long duration = timeInputDuration.getMilliseconds();

        if (duration == 0) {
            new DialogHelper(this, null).showInfoDialog(getString(R.string.duration_null));
            return;
        }

        /* start time and date */
        int hours = timeInputStart.getHours();
        int minutes = timeInputStart.getMinutes();
        int seconds = timeInputStart.getSeconds();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        String timestamp = TimeHelper.createTimestamp(year, month, day, hours, minutes, seconds);

        float distance = Float.parseFloat(editTextDistance.getText().toString()) * 1000.0f;

        boolean update = true;
        if (workout == null)
            update = false;
        workout = new Workout(timestamp, duration, distance, 3, (Sport) sportsSpinner.getSelectedItem());

        if (update) {
            workoutDAO.updateAdd(workout, workoutID);
            finish();
        }
        else {
            workoutID = workoutDAO.add(workout);
            Intent intent = new Intent(this, WorkoutDetailActivity.class);
            intent.putExtra("workout_id", workoutID);
            finish();
        }
    }

    private class DistanceFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int dstart, int dend) {
            try {
                float distance = Float.parseFloat(spanned.toString() + charSequence.toString());
                if (distance >= 0.0f && distance <= 999.9f)
                    return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
