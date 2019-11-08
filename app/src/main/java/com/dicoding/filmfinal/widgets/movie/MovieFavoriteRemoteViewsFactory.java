package com.dicoding.filmfinal.widgets.movie;

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
import com.dicoding.filmfinal.db.repositories.MovieFavoriteRepository;
import com.dicoding.filmfinal.db.room.MovieFavorite;

import java.util.List;

public class MovieFavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private MovieFavoriteRepository movieFavoriteRepository;
    private List<MovieFavorite> movieFavoriteList;
    private final Context context;

    MovieFavoriteRemoteViewsFactory(Application application) {
        context = application.getApplicationContext();
        movieFavoriteRepository = new MovieFavoriteRepository(application);
    }

    @Override
    public void onCreate() {
        movieFavoriteList = movieFavoriteRepository.select();
    }

    @Override
    public void onDataSetChanged() {
        movieFavoriteList = movieFavoriteRepository.select();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieFavoriteList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.items_movie_widget);

        if (movieFavoriteList.size() > 0) {
            MovieFavorite movieFavorite = movieFavoriteList.get(i);

            try {
                Bitmap bitmap = Glide.with(context).asBitmap()
                        .load(CONSTANT.URL_POSTER + movieFavorite.getPoster_movie())
                        .submit(300, 450)
                        .get();

                remoteViews.setImageViewBitmap(R.id.img_poster_widget, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putString(CONSTANT.EXTRA_TITLE, movieFavorite.getTitle_movie());
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
