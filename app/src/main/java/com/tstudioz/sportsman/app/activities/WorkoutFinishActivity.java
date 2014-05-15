package com.tstudioz.sportsman.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.SportsmanApp;
import com.tstudioz.sportsman.app.database.WorkoutDAO;
import com.tstudioz.sportsman.app.training.WorkoutWithWaypoints;

import java.util.ArrayList;
import java.util.List;


public class WorkoutFinishActivity extends Activity {

    private WorkoutWithWaypoints workout;
    private List<ImageButton> moodButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_finish);

        View.OnClickListener clickListener = new MoodClickListener();
        moodButtons = new ArrayList<>(3);
        moodButtons.add((ImageButton) findViewById(R.id.btn_mood_best));
        moodButtons.add((ImageButton) findViewById(R.id.btn_mood_middle));
        moodButtons.add((ImageButton) findViewById(R.id.btn_mood_worse));
        for (int i = 0; i < moodButtons.size(); i++) {
            moodButtons.get(i).setOnClickListener(clickListener);
        }
    }

    private class MoodClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int moodID = 0;
            switch (view.getId()) {
                case R.id.btn_mood_best: {
                    moodID = 3;
                    break;
                }
                case R.id.btn_mood_middle: {
                    moodID = 2;
                    break;
                }
                case R.id.btn_mood_worse: {
                    moodID = 1;
                    break;
                }
            }
            new ProcessWorkout(moodID).execute();
        }
    }

    private class ProcessWorkout extends AsyncTask<Void, Void, Void> {
        private ProcessWorkout(int moodID) {
            this.moodID = moodID;
        }
        private final int moodID;
        private long workoutID;
        @Override
        protected Void doInBackground(Void... voids) {
            workoutID = saveWorkout(moodID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(WorkoutFinishActivity.this, WorkoutDetailActivity.class);
            intent.putExtra("workout_id", workoutID);
            startActivity(intent);
            finish();
        }
    }

    private long saveWorkout(int moodID) {
        workout = ((SportsmanApp) getApplicationContext()).getActiveWorkout();
        workout.setMood(moodID);
        WorkoutDAO workoutDAO = new WorkoutDAO(getApplicationContext());
        return workoutDAO.add(workout);
    }

}
