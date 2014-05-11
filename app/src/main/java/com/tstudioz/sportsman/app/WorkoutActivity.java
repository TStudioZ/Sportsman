package com.tstudioz.sportsman.app;

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


public class WorkoutActivity extends Activity implements
        GooglePlayServicesClient.OnConnectionFailedListener {

    private ConnectionResult connectionResult;
    private Intent serviceIntent;
    private Button btnStartWorkout;
    private Button btnPauseWorkout;
    private Button btnResumeWorkout;
    private Button btnStopWorkout;
    private TextView textViewDistance;
    private TextView textViewTime;
    private TextView textViewSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        ((SportsmanApp) getApplicationContext()).setWorkoutActivity(this);
        serviceIntent = new Intent(this, WorkoutService.class);
        serviceIntent.putExtra("sport_id", getIntent().getExtras().getInt("sport_id"));
        startService(serviceIntent);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("distance_update");
        intentFilter.addAction("time_update");
        workoutBroadcastReceiver = new WorkoutBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(workoutBroadcastReceiver, intentFilter);

        btnStartWorkout = (Button) findViewById(R.id.btn_start_workout);
        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SportsmanApp)getApplicationContext()).getWorkoutService().resumeWorkout();
                btnStartWorkout.setVisibility(View.GONE);
                btnPauseWorkout.setVisibility(View.VISIBLE);
            }
        });

        btnPauseWorkout = (Button) findViewById(R.id.btn_pause_workout);
        btnPauseWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SportsmanApp)getApplicationContext()).getWorkoutService().pauseWorkout();
                findViewById(R.id.start_pause).setVisibility(View.GONE);
                findViewById(R.id.resume_stop).setVisibility(View.VISIBLE);
                textViewSpeed.setText("0.0 m/s");
            }
        });

        btnResumeWorkout = (Button) findViewById(R.id.btn_resume_workout);
        btnResumeWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SportsmanApp)getApplicationContext()).getWorkoutService().resumeWorkout();
                findViewById(R.id.resume_stop).setVisibility(View.GONE);
                findViewById(R.id.start_pause).setVisibility(View.VISIBLE);
                btnStartWorkout.setVisibility(View.GONE);
                btnPauseWorkout.setVisibility(View.VISIBLE);
            }
        });

        btnStopWorkout = (Button) findViewById(R.id.btn_stop_workout);
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
        textViewTime = (TextView) findViewById(R.id.time);
        textViewSpeed = (TextView) findViewById(R.id.speed);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(serviceIntent);
    }

    /**
     * Updates HUD with distance and speed.
     * @param distance taken distance
     * @param speed current speed
     */
    private void updateHUD(float distance, float speed) {
        textViewDistance.setText(String.valueOf(distance) + " m");
        textViewSpeed.setText(String.valueOf(speed) + " m/s");
    }

    /**
     * Updates time of HUD based on parameter.
     * @param time String version of time
     */
    private void updateHUD(String time) {
        textViewTime.setText(time);
    }

    private WorkoutBroadcastReceiver workoutBroadcastReceiver;
    private class WorkoutBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WorkoutService.INTENT_DISTANCE_UPDATE)) {
                float distance = intent.getFloatExtra("distance", 0);
                float speed = intent.getFloatExtra("speed", 0);
                updateHUD(distance, speed);
            }
            else if (intent.getAction().equals(WorkoutService.INTENT_TIME_UPDATE)) {
                String time = intent.getStringExtra("time");
                updateHUD(time);
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
