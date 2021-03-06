package com.onramp.android.takehome.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationService extends Service {

    private final String LOG_TAG = NotificationService.class.getSimpleName();
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private String quoteFrequency;

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Receive the quote frequency from SharedPreferences
        retrieveQuoteFrequencyFromSharedPreferences();
        setupRecurringNotifications();
        //makeApiCall();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.v(LOG_TAG, "OnDestroy Called");
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private void setupRecurringNotifications() {
        long frequencyInMillis = Long.parseLong(quoteFrequency);
        int minutesInMilis = 60000;
        frequencyInMillis *= minutesInMilis;
        Log.v(LOG_TAG, "Frequency IN Millis: " + frequencyInMillis);
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Log.v(LOG_TAG, "Starting Broadcast Receiver");

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + frequencyInMillis,
                frequencyInMillis, pendingIntent);
    }

    private void retrieveQuoteFrequencyFromSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        quoteFrequency = preferences.getString("quote_frequency", "");
        Log.v(LOG_TAG, "Quote Frequency: " + quoteFrequency);

    }
}
