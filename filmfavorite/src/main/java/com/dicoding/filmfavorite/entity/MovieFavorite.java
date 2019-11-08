package com.dicoding.filmfavorite.entity;


public class MovieFavorite {
    private int id_movie;

    private String title_movie;

    private String date_movie;

    private String genre_movie;

    private String description_movie;

    private String poster_movie;

    public MovieFavorite(int id_movie, String title_movie, String date_movie, String genre_movie, String description_movie, String poster_movie) {
        this.id_movie = id_movie;
        this.title_movie = title_movie;
        this.date_movie = date_movie;
        this.genre_movie = genre_movie;
        this.description_movie = description_movie;
        this.poster_movie = poster_movie;
    }

    public int getId_movie() {
        return id_movie;
    }

    public void setId_movie(int id_movie) {
        this.id_movie = id_movie;
    }

    public String getTitle_movie() {
        return title_movie;
    }

    public void setTitle_movie(String title_movie) {
        this.title_movie = title_movie;
    }

    public String getDate_movie() {
        return date_movie;
    }

    public void setDate_movie(String date_movie) {
        this.date_movie = date_movie;
    }

    public String getGenre_movie() {
        return genre_movie;
    }

    public void setGenre_movie(String genre_movie) {
        this.genre_movie = genre_movie;
    }

    public String getDescription_movie() {
        return description_movie;
    }

    public void setDescription_movie(String description_movie) {
        this.description_movie = description_movie;
    }

    public String getPoster_movie() {
        return poster_movie;
    }

    public void setPoster_movie(String poster_movie) {
        this.poster_movie = poster_movie;
    }
}
