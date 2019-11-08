package com.dicoding.filmfinal.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.R;
import com.dicoding.filmfinal.notifications.ReminderReceiver;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        SwitchPreferenceCompat releasePref, dailyPref;
        ReminderReceiver reminderReceiver;
        Context context;
        boolean state;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            releasePref = findPreference("release");
            dailyPref = findPreference("daily");
            reminderReceiver = new ReminderReceiver();

            releasePref.setOnPreferenceClickListener(preference -> {
                state = ((SwitchPreferenceCompat) preference).isChecked();
                if (state) {
                    reminderReceiver.setReleaseTodayReminder(context, "08:00");
                } else {
                    reminderReceiver.cancelReminder(context, CONSTANT.ID_RELEASE);
                }
                return true;
            });

            dailyPref.setOnPreferenceClickListener(preference -> {
                state = ((SwitchPreferenceCompat) preference).isChecked();
                if (state) {
                    String appName = context.getResources().getString(R.string.app_name);
                    reminderReceiver.setDailyReminder(context, "07:00"
                            , appName + " " + context.getString(R.string.missing_you), appName);
                } else {
                    reminderReceiver.cancelReminder(context, CONSTANT.ID_DAILY);
                }
                return true;
            });
        }
    }
}