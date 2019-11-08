package com.dicoding.filmfinal.widgets.movie;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MovieFavoriteWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MovieFavoriteRemoteViewsFactory(this.getApplication());
    }
}
