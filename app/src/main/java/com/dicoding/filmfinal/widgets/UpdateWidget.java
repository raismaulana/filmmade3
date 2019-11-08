package com.dicoding.filmfinal.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import com.dicoding.filmfinal.R;

public class UpdateWidget {
    private Context context;

    public UpdateWidget(Context context) {
        this.context = context;
    }

    public void update(Class mClass) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, mClass));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
    }
}
