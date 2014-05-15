package com.tstudioz.sportsman.app.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tstudioz.sportsman.app.R;
import com.tstudioz.sportsman.app.SportsmanApp;
import com.tstudioz.sportsman.app.adapters.WorkoutCursorAdapter;
import com.tstudioz.sportsman.app.services.WorkoutService;
import com.tstudioz.sportsman.app.training.Sport;


public class WorkoutActivity extends Activity implements
        GooglePlayServicesClient.OnConnectionFailedListener,
        DialogHelper.CommandListener {

    /* result of Google Play Services connection */
    private ConnectionResult connectionResult;
    private Intent serviceIntent;
    private Button btnStartWorkout;
    private Button btnPauseWorkout;
    private Button btnResumeWorkout;
    private Button btnStopWorkout;
    private TextView textViewDistance;
    private TextView textViewDuration;
    private TextView textViewSpeed;
    private TextView textViewSport;
    private int sportID;
    private IntentFilter intentFilter;

    /* mode of the workout - not started, started, paused */
    private int mode;
    private static final int START_MODE = 0;
    private static final int PAUSE_MODE = 1;
    private static final int STOP_MODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mode = 0;
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt("mode", 0);
        }

        btnPauseWorkout = (Button) findViewById(R.id.btn_pause_workout);
        btnStartWorkout = (Button) findViewById(R.id.btn_start_workout);
        btnResumeWorkout = (Button) findViewById(R.id.btn_resume_workout);
        btnStopWorkout = (Button) findViewById(R.id.btn_stop_workout);

        if (mode == PAUSE_MODE)
            showPause();
        else if (mode == STOP_MODE)
            showStop();

        sportID = getIntent().getExtras().getInt("sport_id");
        ((SportsmanApp) getApplicationContext()).setWorkoutActivity(this);
        serviceIntent = new Intent(this, WorkoutService.class);
        serviceIntent.putExtra("sport_id", sportID);
        startService(serviceIntent);

        intentFilter = new IntentFilter();
        intentFilter.addAction("distance_update");
        intentFilter.addAction("time_update");
        workoutBroadcastReceiver = new WorkoutBroadcastReceiver();

        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SportsmanApp)getApplicationContext()).getWorkoutService().resumeWorkout();
                showPause();
            }
        });

        btnPauseWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SportsmanApp)getApplicationContext()).getWorkoutService().pauseWorkout();
                showStop();
                textViewSpeed.setText( String.format("%.2f", 0.0f) + WorkoutCursorAdapter.SPEED_UNITS );
            }
        });

        btnResumeWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SportsmanApp)getApplicationContext()).getWorkoutService().resumeWorkout();
                showPause();
            }
        });

        btnStopWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SportsmanApp)getApplicationContext()).getWorkoutService().stopWorkout();
                stopService(serviceIntent);
                Intent intent = new Intent(WorkoutActivity.this, WorkoutFinishActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textViewDistance = (TextView) findViewById(R.id.distance);
        textViewDuration = (TextView) findViewById(R.id.duration);
        textViewSpeed = (TextView) findViewById(R.id.speed);
        textViewSport = (TextView) findViewById(R.id.text_view_sport);

        textViewSport.setText(Sport.getSport(sportID).getNameID());

        /* reset views if we haven'\t started yet */
        if (mode == START_MODE) {
            updateHUD();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(workoutBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(workoutBroadcastReceiver);
    }

    private final int CANCEL_WORKOUT_COMMAND = 1;
    @Override
    public void onBackPressed() {
        if (mode == START_MODE)
            super.onBackPressed();
        else
            new DialogHelper(this, this)
                    .showYesNoDialog(getString(R.string.cancel_workout),
                            CANCEL_WORKOUT_COMMAND);
    }

    @Override
    public void onCommandSelected(int command) {
        if (command == CANCEL_WORKOUT_COMMAND) {
            stopService(serviceIntent);
            super.onBackPressed();
        }
    }

    private void showPause() {
        mode = PAUSE_MODE;
        findViewById(R.id.resume_stop).setVisibility(View.GONE);
        findViewById(R.id.start_pause).setVisibility(View.VISIBLE);
        btnStartWorkout.setVisibility(View.GONE);
        btnPauseWorkout.setVisibility(View.VISIBLE);
    }

    private void showStop() {
        mode = STOP_MODE;
        btnStartWorkout.setVisibility(View.GONE);
        btnPauseWorkout.setVisibility(View.VISIBLE);
        findViewById(R.id.resume_stop).setVisibility(View.VISIBLE);
        findViewById(R.id.start_pause).setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mode);
        outState.putFloat("distance", distance);
        outState.getFloat("speed", speed);
        outState.putString("duration", duration);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            distance = savedInstanceState.getFloat("distance");
            speed = savedInstanceState.getFloat("speed");
            duration = savedInstanceState.getString("duration");
            updateHUD();
        }
    }

    private void updateHUD() {
        updateHUDDistance();
        updateHUDTime();
    }

    /**
     * Updates HUD with distance and speed.
     */
    private void updateHUDDistance() {
        textViewDistance.setText(String.format("%.2f", distance * 0.001f) + WorkoutCursorAdapter.DISTANCE_UNITS);
        textViewSpeed.setText(String.format("%.2f", speed) + WorkoutCursorAdapter.SPEED_UNITS);
    }

    /**
     * Updates time of HUD based on parameter.
     */
    private void updateHUDTime() {
        textViewDuration.setText(duration);
    }

    private WorkoutBroadcastReceiver workoutBroadcastReceiver;

    private float distance = 0.00f;
    private float speed = 0.00f;
    private String duration = "00:00:00";
    private class WorkoutBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WorkoutService.INTENT_DISTANCE_UPDATE)) {
                distance = intent.getFloatExtra("distance", 0);
                speed = intent.getFloatExtra("speed", 0);
                updateHUDDistance();
            }
            else if (intent.getAction().equals(WorkoutService.INTENT_TIME_UPDATE)) {
                duration = intent.getStringExtra("time");
                updateHUDTime();
            }
        }
    }

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        this.connectionResult = connectionResult;
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONNECTION_FAILURE_RESOLUTION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    connectionResult.startResolutionForResult(
                            this,
                            CONNECTION_FAILURE_RESOLUTION_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            showErrorDialog(connectionResult.getErrorCode());
            return false;
        }
    }

    private void showErrorDialog(int errorCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);
        if (errorDialog != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getFragmentManager(), "Location Updates");
        }
    }
}
