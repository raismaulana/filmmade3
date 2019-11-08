package com.dicoding.filmfinal.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.R;
import com.dicoding.filmfinal.db.models.MovieItems;
import com.dicoding.filmfinal.db.models.ResultMovie;
import com.dicoding.filmfinal.networking.FilmApi;
import com.dicoding.filmfinal.networking.FilmService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderReceiver extends BroadcastReceiver {

    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        int notifId = intent.getIntExtra(CONSTANT.EXTRA_TYPE, 100);
        if (notifId == CONSTANT.ID_DAILY) {
            showReminderNotification(context, intent.getStringExtra(CONSTANT.EXTRA_TITLE)
                    , intent.getStringExtra(CONSTANT.EXTRA_MESSAGE), notifId);
        } else if (notifId == CONSTANT.ID_RELEASE) {
            showReleaseTodayReminder(context, notifId);
        }
    }

    public void setDailyReminder(Context context, String time, String message, String title) {
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(CONSTANT.EXTRA_MESSAGE, message);
        intent.putExtra(CONSTANT.EXTRA_TITLE, title);
        intent.putExtra(CONSTANT.EXTRA_TYPE, CONSTANT.ID_DAILY);

        Log.e("Daily Reminder", time);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, CONSTANT.ID_DAILY, intent, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, R.string.toast_daily_reminder_activated, Toast.LENGTH_SHORT).show();
    }

    public void setReleaseTodayReminder(Context context, String time) {
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(CONSTANT.EXTRA_TYPE, CONSTANT.ID_RELEASE);

        Log.e("Release Reminder", time);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, CONSTANT.ID_RELEASE, intent, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, R.string.toast_release_today_reminder_activated, Toast.LENGTH_SHORT).show();
    }

    private void showReleaseTodayReminder(Context context, int notifyId) {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = date.format(new Date());

        FilmApi filmApi = FilmService.getFilmService().create(FilmApi.class);
        Call<MovieItems> call = filmApi.getNewReleaseMovie(CONSTANT.API_KEY, today, today);
        call.enqueue(new Callback<MovieItems>() {
            @Override
            public void onResponse(@NonNull Call<MovieItems> call, @NonNull Response<MovieItems> response) {
                if (response.body() != null) {
                    List<ResultMovie> resultMovie = response.body().getResults();
                    for (int i = 0; i < resultMovie.size(); i++) {
                        showReminderNotification(context, resultMovie.get(i).getTitle()
                                , resultMovie.get(i).getTitle() + " " + context.getResources().getString(R.string.has_been_released_today)
                                , notifyId + i);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieItems> call, @NonNull Throwable t) {
                Log.e("REMINDER", "onFailure: ", t);
            }
        });
    }

    public void showReminderNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "FILMFINALChannel_1";
        String CHANNEL_NAME = "FilmFinal Reminder channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void cancelReminder(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        if (requestCode == CONSTANT.ID_DAILY) {
            Toast.makeText(context
                    , context.getResources().getString(R.string.toast_daily_reminder_deactivated)
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context
                    , context.getResources().getString(R.string.toast_release_today_reminder_deactivated)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
