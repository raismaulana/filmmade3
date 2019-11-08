package com.dicoding.filmfinal.ui.tv;

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
import com.dicoding.filmfinal.db.models.ResultTV;
import com.dicoding.filmfinal.db.room.TVFavorite;
import com.dicoding.filmfinal.widgets.UpdateWidget;
import com.dicoding.filmfinal.widgets.tv.TVFavoriteWidget;

import java.util.List;

public class TVFragment extends Fragment implements SearchView.OnQueryTextListener {
    private TextView textView;
    private RecyclerView rvTV;
    private TVAdapter tvAdapter;
    private ProgressBar progressBar;
    private TVViewModel tvViewModel;
    private androidx.appcompat.widget.SearchView searchView;
    private String mSearchQuery;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.txt_no_data);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        progressBar = view.findViewById(R.id.progress_bar);
        rvTV = view.findViewById(R.id.rv_tv);
        rvTV.setLayoutManager(new LinearLayoutManager(getActivity()));

        showLoading(true);

        tvViewModel = ViewModelProviders.of(this).get(TVViewModel.class);
        tvViewModel.LANGUAGE = getString(R.string.language_code);
        tvViewModel.getMoviesRepository().observe(this, getTVData);

        showRecycler();

        if (savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(CONSTANT.SEARCH_QUERY);
            if (TextUtils.isEmpty(mSearchQuery)) {
                showLoading(true);
                tvViewModel.setTV();
            }
        } else {
            tvViewModel.setTV();
        }

        swipeRefreshLayout.setOnRefreshListener(() -> tvViewModel.setTV());
    }

    private Observer<List<ResultTV>> getTVData = new Observer<List<ResultTV>>() {
        @Override
        public void onChanged(List<ResultTV> resultMovies) {
            if (resultMovies != null) {
                textView.setVisibility(View.GONE);
                tvAdapter.setData(resultMovies);
            }
            showLoading(false);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private void showRecycler() {
        tvAdapter = new TVAdapter(getContext());
        tvAdapter.notifyDataSetChanged();
        rvTV.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTV.setAdapter(tvAdapter);

        tvAdapter.setOnItemClickCallback((data, btn) -> {
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
                parcel.setTitle_film(data.getName());
                parcel.setPoster_film(data.getPosterPath());
                parcel.setGenre_film(genre);
                parcel.setDescription_film(data.getOverview());
                parcel.setDate_film(data.getFirstAirDate());
                Intent toDetailActivity = new Intent(getContext(), DetailActivity.class);
                toDetailActivity.putExtra(CONSTANT.EXTRA_FILM, parcel);
                startActivity(toDetailActivity);
            } else {
                TVFavorite tvFavorite = new TVFavorite(
                        data.getId(),
                        data.getName(),
                        data.getFirstAirDate(),
                        genre,
                        data.getOverview(),
                        data.getPosterPath()
                );
                tvViewModel.insert(tvFavorite);
                Toast.makeText(getContext(), R.string.toast_success_add_favorite, Toast.LENGTH_SHORT).show();
                UpdateWidget updateWidget = new UpdateWidget(getContext());
                updateWidget.update(TVFavoriteWidget.class);
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TVFragment.class.getSimpleName(), "showLoading: true");
        } else {
            progressBar.setVisibility(View.GONE);
            Log.d(TVFragment.class.getSimpleName(), "showLoading: false");
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
        tvViewModel.searchTV(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        showLoading(true);
        if (TextUtils.isEmpty(s)) {
            tvViewModel.setTV();
        } else {
            showLoading(true);
            tvViewModel.searchTV(s);
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