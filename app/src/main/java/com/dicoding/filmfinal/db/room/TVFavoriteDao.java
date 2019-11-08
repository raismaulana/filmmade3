package com.dicoding.filmfinal.db.room;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TVFavoriteDao {

    @Query("SELECT id_tv, title_tv, date_tv, genre_tv, description_tv, poster_tv FROM tv_table")
    LiveData<List<TVFavorite>> getTVFavorite();

    @Query("SELECT COUNT(id_tv) FROM tv_table WHERE id_tv=:id_tv")
    int checkFavorite(int id_tv);

    @Insert
    void insert(TVFavorite tvFavorite);

    @Delete
    void delete(TVFavorite tvFavorite);

    @Query("SELECT id_tv, title_tv, date_tv, genre_tv, description_tv, poster_tv FROM tv_table")
    List<TVFavorite> select();


    @Query("SELECT id_tv, title_tv, date_tv, genre_tv, description_tv, poster_tv FROM tv_table")
    Cursor cursor_select();
}
