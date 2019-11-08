package com.dicoding.filmfinal.ui.favorite.movie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.R;
import com.dicoding.filmfinal.activities.DetailActivity;
import com.dicoding.filmfinal.db.models.Film;
import com.dicoding.filmfinal.db.room.MovieFavorite;
import com.dicoding.filmfinal.widgets.UpdateWidget;
import com.dicoding.filmfinal.widgets.movie.MovieFavoriteWidget;

import java.util.List;

public class MovieFavoriteFragment extends Fragment {

    private TextView tvNoData;
    private ProgressBar progressBar;
    private RecyclerView rvMovieFavorite;
    private MovieFavoriteAdapter movieFavoriteAdapter;
    private MovieFavoriteViewModel mViewModel;

    public MovieFavoriteFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        tvNoData = view.findViewById(R.id.txt_no_data);
        tvNoData.setVisibility(View.GONE);

        rvMovieFavorite = view.findViewById(R.id.rv_movies_favorite);
        rvMovieFavorite.setLayoutManager(new LinearLayoutManager(getActivity()));

        showLoading(true);

        showRecycler();

        mViewModel = ViewModelProviders.of(this).get(MovieFavoriteViewModel.class);
        mViewModel.getListMovieFavorite().observe(this, getMovieFavoriteData);
    }

    private Observer<List<MovieFavorite>> getMovieFavoriteData = new Observer<List<MovieFavorite>>() {
        @Override
        public void onChanged(List<MovieFavorite> movieFavorites) {
            if (movieFavorites.isEmpty()) {
                movieFavoriteAdapter.setData(movieFavorites);
                tvNoData.setVisibility(View.VISIBLE);
            } else {
                tvNoData.setVisibility(View.GONE);
                movieFavoriteAdapter.setData(movieFavorites);
            }

            showLoading(false);
        }
    };

    private void showRecycler() {
        movieFavoriteAdapter = new MovieFavoriteAdapter(getContext());
        movieFavoriteAdapter.notifyDataSetChanged();
        rvMovieFavorite.setAdapter(movieFavoriteAdapter);

        movieFavoriteAdapter.setOnItemClickCallback((data, btn) -> {
            if (btn.equals("del")) {
                mViewModel.delete(data);
                Toast.makeText(getContext(), R.string.toast_success_delete_from_favorite, Toast.LENGTH_SHORT).show();
                UpdateWidget updateWidget = new UpdateWidget(getContext());
                updateWidget.update(MovieFavoriteWidget.class);
            } else {
                Film parcel = new Film();
                parcel.setTitle_film(data.getTitle_movie());
                parcel.setPoster_film(data.getPoster_movie());
                parcel.setGenre_film(data.getGenre_movie());
                parcel.setDescription_film(data.getDescription_movie());
                parcel.setDate_film(data.getDate_movie());
                Intent toDetailActivity = new Intent(getContext(), DetailActivity.class);
                toDetailActivity.putExtra(CONSTANT.EXTRA_FILM, parcel);
                startActivity(toDetailActivity);
            }

        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(MovieFavoriteFragment.class.getSimpleName(), "showLoading: true");
        } else {
            progressBar.setVisibility(View.GONE);
            Log.d(MovieFavoriteFragment.class.getSimpleName(), "showLoading: false");
        }
    }
}
