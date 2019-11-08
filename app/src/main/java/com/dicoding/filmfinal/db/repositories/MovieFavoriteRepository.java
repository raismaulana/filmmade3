package com.dicoding.filmfinal.db.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dicoding.filmfinal.db.room.FilmDatabase;
import com.dicoding.filmfinal.db.room.MovieFavorite;
import com.dicoding.filmfinal.db.room.MovieFavoriteDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieFavoriteRepository {
    private MovieFavoriteDao movieFavoriteDao;
    private LiveData<List<MovieFavorite>> listMovieFavoites;

    public MovieFavoriteRepository(Application application) {
        FilmDatabase filmDatabase = FilmDatabase.getInstance(application);
        movieFavoriteDao = filmDatabase.movieFavoriteDao();
        listMovieFavoites = movieFavoriteDao.getMovieFavorite();
    }

    public void insert(MovieFavorite movieFavorite) {
        new InsertMovieFavoriteAsyncTask(movieFavoriteDao).execute(movieFavorite);
    }

    public void delete(MovieFavorite movieFavorite) {
        new DeleteMovieFavoriteAsyncTask(movieFavoriteDao).execute(movieFavorite);
    }

    public LiveData<List<MovieFavorite>> getListMovieFavorites() {
        return listMovieFavoites;
    }

    public List<MovieFavorite> select() {
        try {
            return new SelectMovieFavoriteAsyncTask(movieFavoriteDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class InsertMovieFavoriteAsyncTask extends AsyncTask<MovieFavorite, Void, Void> {
        private MovieFavoriteDao movieFavoriteDao;

        InsertMovieFavoriteAsyncTask(MovieFavoriteDao movieFavoriteDao) {
            this.movieFavoriteDao = movieFavoriteDao;
        }

        @Override
        protected Void doInBackground(MovieFavorite... movieFavorites) {
            if (movieFavoriteDao.checkFavorite(movieFavorites[0].getId_movie()) == 0) {
                movieFavoriteDao.insert(movieFavorites[0]);
            }
            return null;
        }
    }

    private static class DeleteMovieFavoriteAsyncTask extends AsyncTask<MovieFavorite, Void, Void> {
        private MovieFavoriteDao movieFavoriteDao;

        DeleteMovieFavoriteAsyncTask(MovieFavoriteDao movieFavoriteDao) {
            this.movieFavoriteDao = movieFavoriteDao;
        }

        @Override
        protected Void doInBackground(MovieFavorite... movieFavorites) {
            if (movieFavoriteDao.checkFavorite(movieFavorites[0].getId_movie()) > 0) {
                movieFavoriteDao.delete(movieFavorites[0]);
            }
            return null;
        }
    }

    private static class SelectMovieFavoriteAsyncTask extends AsyncTask<Void, Void, List<MovieFavorite>> {

        private MovieFavoriteDao movieFavoriteDao;

        SelectMovieFavoriteAsyncTask(MovieFavoriteDao movieFavoriteDao) {
            this.movieFavoriteDao = movieFavoriteDao;
        }

        @Override
        protected List<MovieFavorite> doInBackground(Void... voids) {
            return movieFavoriteDao.select();
        }

    }

}
