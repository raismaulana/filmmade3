package com.dicoding.filmfinal.db.room;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieFavoriteDao {

    @Query("SELECT id_movie, title_movie, date_movie, genre_movie, description_movie, poster_movie FROM movie_table")
    LiveData<List<MovieFavorite>> getMovieFavorite();

    @Query("SELECT COUNT(id_movie) FROM movie_table WHERE id_movie=:id_movie")
    int checkFavorite(int id_movie);

    @Insert
    void insert(MovieFavorite movieFavorite);

    @Delete
    void delete(MovieFavorite movieFavorite);

    @Query("SELECT id_movie, title_movie, date_movie, genre_movie, description_movie, poster_movie FROM movie_table")
    List<MovieFavorite> select();

    @Query("SELECT id_movie, title_movie, date_movie, genre_movie, description_movie, poster_movie FROM movie_table")
    Cursor cursor_select();


}
