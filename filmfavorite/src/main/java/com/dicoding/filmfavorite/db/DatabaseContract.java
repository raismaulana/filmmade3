package com.dicoding.filmfavorite.db;

import android.net.Uri;
import android.provider.BaseColumns;

import com.dicoding.filmfavorite.CONSTANT;

public class DatabaseContract {

    private DatabaseContract() {
    }

    public static final class MovieColumns implements BaseColumns {
        public static final String ID_MOVIE = "id_movie";
        public static final String TITLE_MOVIE = "title_movie";
        public static final String DATE_MOVIE = "date_movie";
        public static final String GENRE_MOVIE = "genre_movie";
        public static final String DESCRIPTION_MOVIE = "description_movie";
        public static final String POSTER_MOVIE = "poster_movie";


        // untuk membuat URI content://com.dicoding.picodiploma.mynotesapp/movie_table
        public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(CONSTANT.SCHEME)
                .authority(CONSTANT.AUTHORITY)
                .appendPath(CONSTANT.MOVIE_TABLE)
                .build();
    }

    public static final class TvColumns implements BaseColumns {
        public static final String ID_TV = "id_tv";
        public static final String TITLE_TV = "title_tv";
        public static final String DATE_TV = "date_tv";
        public static final String GENRE_TV = "genre_tv";
        public static final String DESCRIPTION_TV = "description_tv";
        public static final String POSTER_TV = "poster_tv";

        // untuk membuat URI content://com.dicoding.picodiploma.mynotesapp/tv_table
        public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme(CONSTANT.SCHEME)
                .authority(CONSTANT.AUTHORITY)
                .appendPath(CONSTANT.TV_TABLE)
                .build();
    }

}
