package com.dicoding.filmfavorite.ui.movie;

import android.content.Context;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.filmfavorite.R;
import com.dicoding.filmfavorite.db.DatabaseContract;
import com.dicoding.filmfavorite.entity.MovieFavorite;
import com.dicoding.filmfavorite.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieFragment extends Fragment implements LoadMoviesCallback {

    private TextView tvNoData;
    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;

    public MovieFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        tvNoData = view.findViewById(R.id.txt_no_data);
        tvNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView rvMovie = view.findViewById(R.id.rv_movies);
        movieAdapter = new MovieAdapter(getContext());
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setHasFixedSize(true);
        rvMovie.setAdapter(movieAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity());
        Objects.requireNonNull(getContext()).getContentResolver()
                .registerContentObserver(DatabaseContract.MovieColumns.CONTENT_URI_MOVIE, true, myObserver);
        new LoadMoviesAsyncTask(getActivity(), this).execute();
    }


    @Override
    public void postExecute(List<MovieFavorite> movieFavorites) {
        progressBar.setVisibility(View.GONE);
        if (movieFavorites.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            movieAdapter.setData(movieFavorites);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            movieAdapter.setData(new ArrayList<>());
        }
    }

    private static class LoadMoviesAsyncTask extends AsyncTask<Void, Void, List<MovieFavorite>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;


        private LoadMoviesAsyncTask(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected List<MovieFavorite> doInBackground(Void... voids) {
            return MappingHelper.mapCursorMovieToArrayList(
                    Objects.requireNonNull(weakContext.get().getContentResolver()
                            .query(DatabaseContract.MovieColumns.CONTENT_URI_MOVIE
                            , null, null, null, null))
            );
        }

        @Override
        protected void onPostExecute(List<MovieFavorite> data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }

    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            new LoadMoviesAsyncTask(context, (LoadMoviesCallback) context).execute();
        }
    }
}


interface LoadMoviesCallback {
    void postExecute(List<MovieFavorite> movieFavorites);
}
