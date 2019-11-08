package com.dicoding.filmfinal.ui.movie;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.db.models.MovieItems;
import com.dicoding.filmfinal.db.models.ResultMovie;
import com.dicoding.filmfinal.db.repositories.MovieFavoriteRepository;
import com.dicoding.filmfinal.db.room.MovieFavorite;
import com.dicoding.filmfinal.networking.FilmApi;
import com.dicoding.filmfinal.networking.FilmService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends AndroidViewModel {
    String LANGUAGE;
    private MutableLiveData<List<ResultMovie>> listMovies = new MutableLiveData<>();
    private MovieFavoriteRepository movieFavoriteRepository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieFavoriteRepository = new MovieFavoriteRepository(application);
    }

    void setMovie() {
        FilmApi filmService = FilmService.getFilmService().create(FilmApi.class);
        Call<MovieItems> call = filmService.getMovieList(CONSTANT.API_KEY, LANGUAGE);
        call.enqueue(new Callback<MovieItems>() {
            @Override
            public void onResponse(@NonNull Call<MovieItems> call, @NonNull Response<MovieItems> response) {
                listMovies.postValue(response.body() != null ? response.body().getResults() : null);
            }

            @Override
            public void onFailure(@NonNull Call<MovieItems> call, @NonNull Throwable t) {
                Log.e("Failure", Objects.requireNonNull(t.getMessage()));
            }
        });

    }

    void searchMovie(String query) {
        FilmApi filmService = FilmService.getFilmService().create(FilmApi.class);
        Call<MovieItems> call = filmService.searchMovie(CONSTANT.API_KEY, LANGUAGE, query);
        call.enqueue(new Callback<MovieItems>() {
            @Override
            public void onResponse(@NonNull Call<MovieItems> call, @NonNull Response<MovieItems> response) {
                listMovies.postValue(response.body() != null ? response.body().getResults() : null);
            }

            @Override
            public void onFailure(@NonNull Call<MovieItems> call, @NonNull Throwable t) {
                Log.e("Failure", Objects.requireNonNull(t.getMessage()));
            }
        });

    }

    LiveData<List<ResultMovie>> getMoviesRepository() {
        if (listMovies == null) {
            listMovies = new MutableLiveData<>();

            setMovie();
        }
        return listMovies;
    }


    public void insert(MovieFavorite movieFavorite) {
        movieFavoriteRepository.insert(movieFavorite);
    }
}