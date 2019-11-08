package com.dicoding.filmfinal.db.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dicoding.filmfinal.CONSTANT;

@Entity(tableName = CONSTANT.TV_TABLE)
public class TVFavorite {
    @PrimaryKey
    private int id_tv;

    @ColumnInfo
    private String title_tv;

    @ColumnInfo
    private String date_tv;

    @ColumnInfo
    private String genre_tv;

    @ColumnInfo
    private String description_tv;

    @ColumnInfo
    private String poster_tv;

    public TVFavorite(int id_tv, String title_tv, String date_tv, String genre_tv, String description_tv, String poster_tv) {
        this.id_tv = id_tv;
        this.title_tv = title_tv;
        this.date_tv = date_tv;
        this.genre_tv = genre_tv;
        this.description_tv = description_tv;
        this.poster_tv = poster_tv;
    }

    public int getId_tv() {
        return id_tv;
    }

    public void setId_tv(int id_tv) {
        this.id_tv = id_tv;
    }

    public String getTitle_tv() {
        return title_tv;
    }

    public void setTitle_tv(String title_tv) {
        this.title_tv = title_tv;
    }

    public String getDate_tv() {
        return date_tv;
    }

    public void setDate_tv(String date_tv) {
        this.date_tv = date_tv;
    }

    public String getGenre_tv() {
        return genre_tv;
    }

    public void setGenre_tv(String genre_tv) {
        this.genre_tv = genre_tv;
    }

    public String getDescription_tv() {
        return description_tv;
    }

    public void setDescription_tv(String description_tv) {
        this.description_tv = description_tv;
    }

    public String getPoster_tv() {
        return poster_tv;
    }

    public void setPoster_tv(String poster_tv) {
        this.poster_tv = poster_tv;
    }
}
