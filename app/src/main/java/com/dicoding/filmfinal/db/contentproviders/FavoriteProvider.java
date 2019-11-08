package com.dicoding.filmfinal.db.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.db.room.FilmDatabase;
import com.dicoding.filmfinal.db.room.MovieFavoriteDao;
import com.dicoding.filmfinal.db.room.TVFavoriteDao;

import java.util.concurrent.ExecutionException;

public class FavoriteProvider extends ContentProvider {
    MovieFavoriteDao movieFavoriteDao;
    TVFavoriteDao tvFavoriteDao;

    private static final int MOVIE = 1;
    private static final int TV = 3;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.dicoding.picodiploma.mynotesapp/movie
        sUriMatcher.addURI(CONSTANT.AUTHORITY, CONSTANT.MOVIE_TABLE, MOVIE);

        sUriMatcher.addURI(CONSTANT.AUTHORITY, CONSTANT.TV_TABLE, TV);
    }


    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        FilmDatabase filmDatabase = FilmDatabase.getInstance(getContext());
        movieFavoriteDao = filmDatabase.movieFavoriteDao();
        tvFavoriteDao = filmDatabase.tvFavoriteDao();
        return true;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                try {
                    cursor = new CursorSelectMovieFavoriteAsyncTask(movieFavoriteDao).execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case TV:
                try {
                    cursor = new CursorSelectTvFavoriteAsyncTask(tvFavoriteDao).execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static class CursorSelectMovieFavoriteAsyncTask extends AsyncTask<Void, Void, Cursor> {

        private MovieFavoriteDao movieFavoriteDao;

        CursorSelectMovieFavoriteAsyncTask(MovieFavoriteDao movieFavoriteDao) {
            this.movieFavoriteDao = movieFavoriteDao;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return movieFavoriteDao.cursor_select();
        }
    }

    private static class CursorSelectTvFavoriteAsyncTask extends AsyncTask<Void, Void, Cursor> {

        private TVFavoriteDao tvFavoriteDao;

        CursorSelectTvFavoriteAsyncTask(TVFavoriteDao tvFavoriteDao) {
            this.tvFavoriteDao = tvFavoriteDao;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return tvFavoriteDao.cursor_select();
        }
    }

}
