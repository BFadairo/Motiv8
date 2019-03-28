package com.onramp.android.takehome.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.onramp.android.takehome.R;
import com.onramp.android.takehome.data.Retrofit.GetQuoteData;
import com.onramp.android.takehome.data.Retrofit.RetrofitInstance;
import com.onramp.android.takehome.model.Quote;
import com.onramp.android.takehome.view.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class AlarmReceiver extends BroadcastReceiver {

    private final String LOG_TAG = AlarmReceiver.class.getSimpleName();
    private Context mContext;
    private Quote quote;
    private String quoteCategory;

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        mContext = context;
        //get Shared Preferences category value
        receiveCategoryFromSharedPreferences();
        //Make an API request to fetch the quote
        fetchQuote();
    }


    @EverythingIsNonNull
    private void fetchQuote() {
        GetQuoteData quoteData = RetrofitInstance.getRetrofitInstance().create(GetQuoteData.class);
        Call<List<Quote>> quoteCall = quoteData.getQuote(quoteCategory, 1, "medium");
        Log.v(LOG_TAG, "" + quoteCall.request().url());

        quoteCall.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (response.isSuccessful()) {
                    quote = response.body().get(0);
                    createNotification(quote);
                    //quote = response.body();
                    Log.v(LOG_TAG, "Call was successful");

                }
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                Log.v(LOG_TAG, t.getMessage());
            }
        });
    }

    private void createNotification(Quote quote) {
        RemoteViews bigPictureView = new RemoteViews(mContext.getPackageName(),
                R.layout.custom_notification);

        Intent intent1 = new Intent(mContext, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent1, 0);
        Bitmap notificationBitmap = null;
        try {
            notificationBitmap = new PicassoAsyncTask(quote).execute(quote).get();
            Log.v(LOG_TAG, "Bitmap Height: " + notificationBitmap.getHeight());
            Log.v(LOG_TAG, "Bitmap Width: " + notificationBitmap.getWidth());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        bigPictureView.setImageViewBitmap(R.id.big_picture_view, notificationBitmap);
        bigPictureView.setTextViewText(R.id.notification_author, quote.getAuthor());
        bigPictureView.setTextViewText(R.id.notification_title, quote.getTitle());
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Service.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "1";
        Notification notification = new Notification.Builder(mContext, CHANNEL_ID)
                .setContentTitle(quote.getAuthor())
                .setContentText(quote.getTitle())
                .setCustomBigContentView(bigPictureView)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(notificationBitmap))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_chat_bubble_black_24dp)
                .build();

        notificationManager.notify(0, notification);
    }

    private static class PicassoAsyncTask extends AsyncTask<Quote, Void, Bitmap> {
        private Quote quote;

        private PicassoAsyncTask(Quote quote) {
            this.quote = quote;
        }

        @Override
        protected Bitmap doInBackground(Quote... quotes) {
            Bitmap picassoImage = null;
            try {
                picassoImage = Picasso.get().load(quote.getQuoteLink()).resize(550, 300).get();
                //picassoImage = resizeBitmap(picassoImage, 720);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return picassoImage;
        }
    }

    private void receiveCategoryFromSharedPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        quoteCategory = preferences.getString("quote_category", "");
        Log.v(LOG_TAG, quoteCategory);
    }
}
