package com.dicoding.filmfinal.widgets.tv;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TVFavoriteWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TVFavoriteRemoteViewsFactory(this.getApplication());
    }
}
