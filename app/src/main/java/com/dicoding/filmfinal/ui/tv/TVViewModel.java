package com.dicoding.filmfinal.ui.tv;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.db.models.ResultTV;
import com.dicoding.filmfinal.db.models.TVItems;
import com.dicoding.filmfinal.db.repositories.TVFavoriteRepository;
import com.dicoding.filmfinal.db.room.TVFavorite;
import com.dicoding.filmfinal.networking.FilmApi;
import com.dicoding.filmfinal.networking.FilmService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVViewModel extends AndroidViewModel {
    private TVFavoriteRepository tvFavoriteRepository;
    String LANGUAGE;
    private MutableLiveData<List<ResultTV>> listTVs = new MutableLiveData<>();

    public TVViewModel(@NonNull Application application) {
        super(application);
        this.tvFavoriteRepository = new TVFavoriteRepository(application);
    }

    void setTV() {
        FilmApi service = FilmService.getFilmService().create(FilmApi.class);
        Call<TVItems> call = service.getTVList(CONSTANT.API_KEY, LANGUAGE);
        call.enqueue(new Callback<TVItems>() {
            @Override
            public void onResponse(@NonNull Call<TVItems> call, @NonNull Response<TVItems> response) {
                listTVs.postValue(response.body() != null ? response.body().getResults() : null);
            }

            @Override
            public void onFailure(@NonNull Call<TVItems> call, @NonNull Throwable t) {
                Log.e("Failure", Objects.requireNonNull(t.getMessage()));
            }
        });

    }

    void searchTV(String query) {
        FilmApi service = FilmService.getFilmService().create(FilmApi.class);
        Call<TVItems> call = service.searchTV(CONSTANT.API_KEY, LANGUAGE, query);
        call.enqueue(new Callback<TVItems>() {
            @Override
            public void onResponse(@NonNull Call<TVItems> call, @NonNull Response<TVItems> response) {
                listTVs.postValue(response.body() != null ? response.body().getResults() : null);
            }

            @Override
            public void onFailure(@NonNull Call<TVItems> call, @NonNull Throwable t) {
                Log.e("Failure", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    LiveData<List<ResultTV>> getMoviesRepository() {
        if (listTVs == null) {
            listTVs = new MutableLiveData<>();

            setTV();
        }
        return listTVs;
    }

    public void insert(TVFavorite tvFavorite) {
        tvFavoriteRepository.insert(tvFavorite);
    }
}