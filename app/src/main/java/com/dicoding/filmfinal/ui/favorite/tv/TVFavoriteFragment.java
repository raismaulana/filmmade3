package com.dicoding.filmfinal.ui.favorite.tv;

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
import com.dicoding.filmfinal.db.room.TVFavorite;
import com.dicoding.filmfinal.widgets.UpdateWidget;
import com.dicoding.filmfinal.widgets.tv.TVFavoriteWidget;

import java.util.List;

public class TVFavoriteFragment extends Fragment {

    private TextView textView;
    private ProgressBar progressBar;
    private RecyclerView rvTVFavorite;
    private TVFavoriteAdapter tvFavoriteAdapter;
    private TVFavoriteViewModel mViewModel;

    public TVFavoriteFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        textView = view.findViewById(R.id.txt_no_data);
        textView.setVisibility(View.GONE);

        rvTVFavorite = view.findViewById(R.id.rv_tv_favorite);
        rvTVFavorite.setLayoutManager(new LinearLayoutManager(getActivity()));

        showLoading(true);

        showRecycler();

        mViewModel = ViewModelProviders.of(this).get(TVFavoriteViewModel.class);
        mViewModel.getListTVFavorite().observe(this, getTVFavoriteData);
    }

    private Observer<List<TVFavorite>> getTVFavoriteData = new Observer<List<TVFavorite>>() {
        @Override
        public void onChanged(List<TVFavorite> tvFavorites) {
            if (tvFavorites.isEmpty()) {
                tvFavoriteAdapter.setData(tvFavorites);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
                tvFavoriteAdapter.setData(tvFavorites);
            }
            showLoading(false);
        }
    };

    private void showRecycler() {
        tvFavoriteAdapter = new TVFavoriteAdapter(getContext());
        tvFavoriteAdapter.notifyDataSetChanged();
        rvTVFavorite.setAdapter(tvFavoriteAdapter);

        tvFavoriteAdapter.setOnItemClickCallback((data, btn) -> {
            if (btn.equals("del")) {
                mViewModel.delete(data);
                Toast.makeText(getContext(), R.string.toast_success_delete_from_favorite, Toast.LENGTH_SHORT).show();
                UpdateWidget updateWidget = new UpdateWidget(getContext());
                updateWidget.update(TVFavoriteWidget.class);
            } else {
                Film parcel = new Film();
                parcel.setTitle_film(data.getTitle_tv());
                parcel.setPoster_film(data.getPoster_tv());
                parcel.setGenre_film(data.getGenre_tv());
                parcel.setDescription_film(data.getDescription_tv());
                parcel.setDate_film(data.getDate_tv());
                Intent toDetailActivity = new Intent(getContext(), DetailActivity.class);
                toDetailActivity.putExtra(CONSTANT.EXTRA_FILM, parcel);
                startActivity(toDetailActivity);
            }

        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TVFavoriteFragment.class.getSimpleName(), "showLoading: true");
        } else {
            progressBar.setVisibility(View.GONE);
            Log.d(TVFavoriteFragment.class.getSimpleName(), "showLoading: false");
        }
    }
}
