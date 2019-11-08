package com.dicoding.filmfinal.db.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MovieFavorite.class, TVFavorite.class}, version = 1)
public abstract class FilmDatabase extends RoomDatabase {
    private static FilmDatabase INSTANCE;

    public abstract MovieFavoriteDao movieFavoriteDao();

    public abstract TVFavoriteDao tvFavoriteDao();

    public static synchronized FilmDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FilmDatabase.class, "film_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
