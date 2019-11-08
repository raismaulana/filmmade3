package com.dicoding.filmfinal.ui.movie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.R;
import com.dicoding.filmfinal.activities.DetailActivity;
import com.dicoding.filmfinal.db.models.Film;
import com.dicoding.filmfinal.db.models.ResultMovie;
import com.dicoding.filmfinal.db.room.MovieFavorite;
import com.dicoding.filmfinal.widgets.UpdateWidget;
import com.dicoding.filmfinal.widgets.movie.MovieFavoriteWidget;

import java.util.List;

public class MovieFragment extends Fragment implements SearchView.OnQueryTextListener {

    private TextView textView;
    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private MovieViewModel movieViewModel;
    private SearchView searchView;
    private String mSearchQuery;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MovieFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.txt_no_data);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        progressBar = view.findViewById(R.id.progress_bar);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));

        showLoading(true);
        showRecycler();

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.LANGUAGE = getString(R.string.language_code);
        movieViewModel.getMoviesRepository().observe(getViewLifecycleOwner(), getMoviesData);


        swipeRefreshLayout.setOnRefreshListener(() -> movieViewModel.setMovie());

        if (savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(CONSTANT.SEARCH_QUERY);
            if (TextUtils.isEmpty(mSearchQuery)) {
                showLoading(true);
                movieViewModel.setMovie();
            }
        } else {
            movieViewModel.setMovie();
        }
    }

    private Observer<List<ResultMovie>> getMoviesData = new Observer<List<ResultMovie>>() {
        @Override
        public void onChanged(List<ResultMovie> resultMovies) {
            if (resultMovies != null) {
                textView.setVisibility(View.GONE);
                movieAdapter.setData(resultMovies);
            }

            showLoading(false);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private void showRecycler() {
        movieAdapter = new MovieAdapter(getContext());
        movieAdapter.notifyDataSetChanged();
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovies.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickCallback((data, btn) -> {
            List genres = data.getGenreIds();
            StringBuilder sb = new StringBuilder();
            for (Object s : genres) {
                sb.append("{");
                sb.append(s);
                sb.append("}");
            }
            String genre = sb.toString();

            if (btn.equals("item")) {
                Film parcel = new Film();
                parcel.setTitle_film(data.getTitle());
                parcel.setPoster_film(data.getPosterPath());
                parcel.setGenre_film(genre);
                parcel.setDescription_film(data.getOverview());
                parcel.setDate_film(data.getReleaseDate());
                Intent toDetailActivity = new Intent(getContext(), DetailActivity.class);
                toDetailActivity.putExtra(CONSTANT.EXTRA_FILM, parcel);
                startActivity(toDetailActivity);

            } else {
                MovieFavorite movieFavorite = new MovieFavorite(
                        data.getId(),
                        data.getTitle(),
                        data.getReleaseDate(),
                        genre,
                        data.getOverview(),
                        data.getPosterPath()
                );
                movieViewModel.insert(movieFavorite);
                Toast.makeText(getContext(), R.string.toast_success_add_favorite, Toast.LENGTH_SHORT).show();
                UpdateWidget updateWidget = new UpdateWidget(getContext());
                updateWidget.update(MovieFavoriteWidget.class);
            }
        });

    }


    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(MovieFragment.class.getSimpleName(), "showLoading: true");
        } else {
            progressBar.setVisibility(View.GONE);
            Log.d(MovieFragment.class.getSimpleName(), "showLoading: false");
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);

        if (!TextUtils.isEmpty(mSearchQuery)) {
            searchView.setIconified(false);
            searchView.setQuery(mSearchQuery, false);
            showLoading(false);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        showLoading(true);
        movieViewModel.searchMovie(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            showLoading(true);
            movieViewModel.setMovie();
        } else {
            showLoading(true);
            movieViewModel.searchMovie(s);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (searchView != null) {
            mSearchQuery = searchView.getQuery().toString();
            outState.putString(CONSTANT.SEARCH_QUERY, mSearchQuery);
        }
    }
}