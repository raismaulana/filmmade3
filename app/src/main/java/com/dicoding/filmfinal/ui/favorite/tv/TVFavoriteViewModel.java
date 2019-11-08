package com.dicoding.filmfinal.ui.favorite.tv;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dicoding.filmfinal.db.repositories.TVFavoriteRepository;
import com.dicoding.filmfinal.db.room.TVFavorite;

import java.util.List;

public class TVFavoriteViewModel extends AndroidViewModel {
    private TVFavoriteRepository tvFavoriteRepository;
    private LiveData<List<TVFavorite>> listTVFavorite;

    public TVFavoriteViewModel(@NonNull Application application) {
        super(application);
        tvFavoriteRepository = new TVFavoriteRepository(application);
        listTVFavorite = tvFavoriteRepository.getListTVFavorites();
    }

    public void insert(TVFavorite tvFavorite) {
        tvFavoriteRepository.insert(tvFavorite);
    }

    public void delete(TVFavorite tvFavorite) {
        tvFavoriteRepository.delete(tvFavorite);
    }

    LiveData<List<TVFavorite>> getListTVFavorite() {
        return listTVFavorite;
    }
}
