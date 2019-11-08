package com.dicoding.filmfavorite.helper;

import android.database.Cursor;

import com.dicoding.filmfavorite.entity.MovieFavorite;
import com.dicoding.filmfavorite.entity.TVFavorite;

import java.util.ArrayList;
import java.util.List;

import static com.dicoding.filmfavorite.db.DatabaseContract.MovieColumns;
import static com.dicoding.filmfavorite.db.DatabaseContract.TvColumns;

public class MappingHelper {

    public static List<MovieFavorite> mapCursorMovieToArrayList(Cursor moviesCursor) {
        List<MovieFavorite> list = new ArrayList<>();

        while (moviesCursor.moveToNext()) {
            int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(MovieColumns.ID_MOVIE));
            String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(MovieColumns.TITLE_MOVIE));
            String description = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(MovieColumns.DESCRIPTION_MOVIE));
            String genre = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(MovieColumns.GENRE_MOVIE));
            String date = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(MovieColumns.DATE_MOVIE));
            String poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(MovieColumns.POSTER_MOVIE));

            list.add(new MovieFavorite(id, title, date, genre, description, poster));
        }

        return list;
    }

    public static List<TVFavorite> mapCursorTvToArrayList(Cursor moviesCursor) {
        List<TVFavorite> list = new ArrayList<>();

        while (moviesCursor.moveToNext()) {
            int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(TvColumns.ID_TV));
            String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TvColumns.TITLE_TV));
            String description = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TvColumns.DESCRIPTION_TV));
            String genre = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TvColumns.GENRE_TV));
            String date = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TvColumns.DATE_TV));
            String poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TvColumns.POSTER_TV));

            list.add(new TVFavorite(id, title, date, genre, description, poster));
        }

        return list;
    }

}
