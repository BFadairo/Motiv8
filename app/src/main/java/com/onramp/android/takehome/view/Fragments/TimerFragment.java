package com.onramp.android.takehome.view.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.onramp.android.takehome.R;
import com.onramp.android.takehome.services.AlarmReceiver;
import com.onramp.android.takehome.services.NotificationService;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TimerFragment extends Fragment {

    private final String LOG_TAG = TimerFragment.class.getSimpleName();
    @BindView(R.id.time_picker)
    TimePicker timePicker;
    @BindView(R.id.start_time_button)
    Button timerStartButton;
    @BindView(R.id.time_difference_hours)
    TextView timeDifferenceHours;
    private CountDownTimer countDownTimer;
    private static final int hoursInMilis = 3600000;
    private static final int minutesInMilis = 60000;
    private long desiredTimeInMillis;
    private Intent serviceIntent;
    private AlarmReceiver alarmReceiver;
    private final String STOP_ACTION = "com.onramp.android.takehome.STOP_ALARM";

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer_fragment, container, false);
        ButterKnife.bind(this, rootView);
        grabTimePickerChoice();
        setupTimerButton();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void grabTimePickerChoice() {
        Log.v(LOG_TAG, "Time Picker Hour: " + timePicker.getHour());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                //Calculate the current time difference
                calculateTime(i, i1);
                //Stops the timer if the user changes the time
                if (countDownTimer != null) {
                    stopTimer();
                    getActivity().stopService(serviceIntent);
                }
            }
        });
    }

    private void setupTimerButton() {
        int currentHour = timePicker.getHour();
        int currentMin = timePicker.getMinute();
        timerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer(desiredTimeInMillis);
                Toast.makeText(getContext(), "GO GO GO!", Toast.LENGTH_SHORT).show();
                serviceIntent = new Intent(getActivity(), NotificationService.class);
                getActivity().startService(serviceIntent);
            }
        });
    }

    private void calculateTime(int hour, int minute) {
        long currentTime = System.currentTimeMillis();
        long timeDifferenceInMilliSeconds;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        timeDifferenceInMilliSeconds = calendar.getTimeInMillis() - currentTime;
        if (timeDifferenceInMilliSeconds > 0) {
            desiredTimeInMillis = timeDifferenceInMilliSeconds;
        } else {
            desiredTimeInMillis = timeDifferenceInMilliSeconds + 86400000;
        }

        Log.v(LOG_TAG, "Time Difference in Millis: " + timeDifferenceInMilliSeconds);

        timeDifferenceHours.setText(String.format(Locale.US, "%02d:%02d", desiredTimeInMillis / hoursInMilis
                , (desiredTimeInMillis % hoursInMilis) / minutesInMilis));
    }

    private void startTimer(long timeDiff) {
        countDownTimer = new CountDownTimer(timeDiff, 1000) {
            @Override
            public void onTick(long l) {
                desiredTimeInMillis = l;
                Log.v(LOG_TAG, " " + l);
                updateTextViewWithTimer();
            }

            @Override
            public void onFinish() {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(1000, 1));
                if (serviceIntent != null) {
                    getActivity().stopService(serviceIntent);
                }
            }
        }.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
    }

    private void updateTextViewWithTimer() {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(desiredTimeInMillis) - TimeUnit.HOURS.toMinutes
                (TimeUnit.MILLISECONDS.toHours(desiredTimeInMillis));
        int seconds = (int) (desiredTimeInMillis / 1000) % 60;
        int hours = (int) (desiredTimeInMillis / hoursInMilis);

        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        timeDifferenceHours.setText(time);
    }

}
