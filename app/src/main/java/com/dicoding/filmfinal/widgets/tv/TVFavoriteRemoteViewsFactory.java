package com.dicoding.filmfinal.widgets.tv;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.R;
import com.dicoding.filmfinal.db.repositories.TVFavoriteRepository;
import com.dicoding.filmfinal.db.room.TVFavorite;

import java.util.List;

public class TVFavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private TVFavoriteRepository tvFavoriteRepository;
    private List<TVFavorite> tvFavoriteList;
    private final Context context;

    TVFavoriteRemoteViewsFactory(Application application) {
        context = application.getApplicationContext();
        tvFavoriteRepository = new TVFavoriteRepository(application);
    }

    @Override
    public void onCreate() {
        tvFavoriteList = tvFavoriteRepository.select();
    }

    @Override
    public void onDataSetChanged() {
        tvFavoriteList = tvFavoriteRepository.select();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return tvFavoriteList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.items_tv_widget);

        if (tvFavoriteList.size() > 0) {
            TVFavorite tvFavorite = tvFavoriteList.get(i);

            try {
                Bitmap bitmap = Glide.with(context).asBitmap()
                        .load(CONSTANT.URL_POSTER + tvFavorite.getPoster_tv())
                        .submit(300, 450)
                        .get();

                remoteViews.setImageViewBitmap(R.id.img_poster_widget, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putString(CONSTANT.EXTRA_TITLE, tvFavorite.getTitle_tv());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(bundle);
            remoteViews.setOnClickFillInIntent(R.id.img_poster_widget, fillInIntent);
            return remoteViews;
        } else {
            return null;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
