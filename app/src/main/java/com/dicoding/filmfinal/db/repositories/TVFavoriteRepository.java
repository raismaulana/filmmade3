package com.dicoding.filmfinal.db.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dicoding.filmfinal.db.room.FilmDatabase;
import com.dicoding.filmfinal.db.room.TVFavorite;
import com.dicoding.filmfinal.db.room.TVFavoriteDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TVFavoriteRepository {
    private TVFavoriteDao tvFavoriteDao;
    private LiveData<List<TVFavorite>> listTVFavorites;

    public TVFavoriteRepository(Application application) {
        FilmDatabase filmDatabase = FilmDatabase.getInstance(application);
        tvFavoriteDao = filmDatabase.tvFavoriteDao();
        listTVFavorites = tvFavoriteDao.getTVFavorite();
    }

    public void insert(TVFavorite tvFavorite) {
        new InsertTVFavoriteAsynkTask(tvFavoriteDao).execute(tvFavorite);
    }

    public void delete(TVFavorite tvFavorite) {
        new DeleteTVFavoriteAsynkTask(tvFavoriteDao).execute(tvFavorite);
    }

    public List<TVFavorite> select() {
        try {
            return new SelectTVFavoriteAsyncTask(tvFavoriteDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<TVFavorite>> getListTVFavorites() {
        return listTVFavorites;
    }

    private static class InsertTVFavoriteAsynkTask extends AsyncTask<TVFavorite, Void, Void> {
        private TVFavoriteDao tvFavoriteDao;

        InsertTVFavoriteAsynkTask(TVFavoriteDao tvFavoriteDao) {
            this.tvFavoriteDao = tvFavoriteDao;
        }

        @Override
        protected Void doInBackground(TVFavorite... tvFavorites) {
            if (tvFavoriteDao.checkFavorite(tvFavorites[0].getId_tv()) == 0) {
                tvFavoriteDao.insert(tvFavorites[0]);
            }
            return null;
        }
    }

    private static class DeleteTVFavoriteAsynkTask extends AsyncTask<TVFavorite, Void, Void> {
        private TVFavoriteDao tvFavoriteDao;

        DeleteTVFavoriteAsynkTask(TVFavoriteDao tvFavoriteDao) {
            this.tvFavoriteDao = tvFavoriteDao;
        }

        @Override
        protected Void doInBackground(TVFavorite... tvFavorites) {
            if (tvFavoriteDao.checkFavorite(tvFavorites[0].getId_tv()) > 0) {
                tvFavoriteDao.delete(tvFavorites[0]);
            }
            return null;
        }
    }

    private static class SelectTVFavoriteAsyncTask extends AsyncTask<Void, Void, List<TVFavorite>> {

        private TVFavoriteDao tvFavoriteDao;

        SelectTVFavoriteAsyncTask(TVFavoriteDao tvFavoriteDao) {
            this.tvFavoriteDao = tvFavoriteDao;
        }

        @Override
        protected List<TVFavorite> doInBackground(Void... voids) {
            return tvFavoriteDao.select();
        }

    }

}
