package com.dicoding.filmfinal.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dicoding.filmfinal.R;
import com.dicoding.filmfinal.ui.favorite.movie.MovieFavoriteFragment;
import com.dicoding.filmfinal.ui.favorite.tv.TVFavoriteFragment;
import com.google.android.material.tabs.TabLayout;

public class FavoriteFragment extends Fragment {

    private TabLayout tabLayout;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager) {
        assert getFragmentManager() != null;
        FavoriteViewPagerAdapter favoritePagerAdapter = new FavoriteViewPagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        favoritePagerAdapter.addFragment(new MovieFavoriteFragment(), getString(R.string.title_movie));
        favoritePagerAdapter.addFragment(new TVFavoriteFragment(), getString(R.string.title_tv));
        viewPager.setAdapter(favoritePagerAdapter);
    }
}