package com.dicoding.filmfinal.ui.favorite.movie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dicoding.filmfinal.db.repositories.MovieFavoriteRepository;
import com.dicoding.filmfinal.db.room.MovieFavorite;

import java.util.List;

public class MovieFavoriteViewModel extends AndroidViewModel {
    private LiveData<List<MovieFavorite>> listMovieFavorite;
    private MovieFavoriteRepository movieFavoriteRepository;

    public MovieFavoriteViewModel(@NonNull Application application) {
        super(application);
        movieFavoriteRepository = new MovieFavoriteRepository(application);
        listMovieFavorite = movieFavoriteRepository.getListMovieFavorites();
    }

    public void insert(MovieFavorite movieFavorite) {
        movieFavoriteRepository.insert(movieFavorite);
    }

    public void delete(MovieFavorite movieFavorite) {
        movieFavoriteRepository.delete(movieFavorite);
    }

    LiveData<List<MovieFavorite>> getListMovieFavorite() {
        return listMovieFavorite;
    }
}
