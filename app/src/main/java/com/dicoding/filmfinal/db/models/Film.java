package com.dicoding.filmfinal.db.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable {
    private int id_film;
    private String title_film;
    private String date_film;
    private String genre_film;
    private String description_film;
    private String poster_film;

    public Film() {
    }

    public String getTitle_film() {
        return title_film;
    }

    public void setTitle_film(String title_film) {
        this.title_film = title_film;
    }

    public String getDate_film() {
        return date_film;
    }

    public void setDate_film(String date_film) {
        this.date_film = date_film;
    }

    public String getGenre_film() {
        return genre_film;
    }

    public void setGenre_film(String genre_film) {
        this.genre_film = genre_film;
    }

    public String getDescription_film() {
        return description_film;
    }

    public void setDescription_film(String description_film) {
        this.description_film = description_film;
    }

    public String getPoster_film() {
        return poster_film;
    }

    public void setPoster_film(String poster_film) {
        this.poster_film = poster_film;
    }

    private Film(Parcel in) {
        id_film = in.readInt();
        title_film = in.readString();
        date_film = in.readString();
        genre_film = in.readString();
        description_film = in.readString();
        poster_film = in.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_film);
        parcel.writeString(title_film);
        parcel.writeString(date_film);
        parcel.writeString(genre_film);
        parcel.writeString(description_film);
        parcel.writeString(poster_film);
    }
}
