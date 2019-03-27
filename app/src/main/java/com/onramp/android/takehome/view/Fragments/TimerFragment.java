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
import com.onramp.android.takehome.services.MyAlarmReceiver;
import com.onramp.android.takehome.services.MyTaskService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private final String STOP_ACTION = "com.onramp.android.takehome.STOP_ALARM";

    int currentHour, currentMin;

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer_fragment, container, false);
        ButterKnife.bind(this, rootView);
        getTheCurrentTime();
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
                Log.v(LOG_TAG, "Current Time: " + currentHour + ":" + currentMin);
                calculateTime(i, i1);
                Log.v(LOG_TAG, "First int : " + i);
                Log.v(LOG_TAG, "Second int : " + i1);
                if (countDownTimer != null) {
                    stopTimer();
                    getActivity().stopService(serviceIntent);
                }
            }
        });
    }

    public void getTheCurrentTime() {
        String currentDate = Calendar.getInstance().getTime().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d k:mm:ss z yyyy");
        SimpleDateFormat wantedFormated = new SimpleDateFormat("h:mm a");
        Date date;
        try {
            date = simpleDateFormat.parse(currentDate);
            Log.v(LOG_TAG, "This is the date: " + date.toString());
            String formattedTime = wantedFormated.format(date);
            Log.v(LOG_TAG, "This is the date: " + wantedFormated.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setupTimerButton() {
        currentHour = timePicker.getHour();
        currentMin = timePicker.getMinute();
        timerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer(desiredTimeInMillis);
                Toast.makeText(getContext(), "GO GO GO!", Toast.LENGTH_SHORT).show();
                serviceIntent = new Intent(getActivity(), MyTaskService.class);
                getActivity().startService(serviceIntent);
            }
        });
    }

    private void calculateTime(int hour, int minute) {
        int hourDifference;
        int minDifference;
        long currentTime = System.currentTimeMillis();
        long diffTime = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        diffTime = calendar.getTimeInMillis() - currentTime;
        desiredTimeInMillis = diffTime;
        Log.v(LOG_TAG, "Time Difference in Millis: " + diffTime);

        /*if (hour > currentHour){
            hourDifference = hour - currentHour;
        } else {
            hourDifference = currentHour - hour;
        }

        if (minute > currentMin) {
            minDifference = minute - currentMin;
        } else {
            minDifference = currentMin - minute;
        }*/

        //Log.v(LOG_TAG, "Hour String: " + hourDifference);
        //Log.v(LOG_TAG, "Minute String: " + minDifference);
        //timeDifferenceHours.setText(String.valueOf(hourDifference).trim());
        //timeDifferenceMins.setText(String.valueOf(minDifference).trim());
        timeDifferenceHours.setText(String.format(Locale.US, "%02d:%02d", diffTime / hoursInMilis
                , (diffTime % hoursInMilis) / minutesInMilis));
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
                getActivity().stopService(serviceIntent);
            }
        }.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
    }

    private void updateTextViewWithTimer() {
        int minutes = (int) (desiredTimeInMillis / 1000) / 60;
        int seconds = (int) (desiredTimeInMillis / 1000) % 60;
        int hours = (int) (desiredTimeInMillis / hoursInMilis);

        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        timeDifferenceHours.setText(time);
    }


}
