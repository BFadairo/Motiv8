package com.onramp.android.takehome.view.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.onramp.android.takehome.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TimerFragment extends Fragment {

    private final String LOG_TAG = TimerFragment.class.getSimpleName();
    @BindView(R.id.time_picker)
    TimePicker timePicker;

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
                Log.v(LOG_TAG, "First int : " + i);
                Log.v(LOG_TAG, "Second int : " + i1);
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


}
