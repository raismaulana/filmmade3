package com.dicoding.filmfinal.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.R;
import com.dicoding.filmfinal.db.models.Film;

public class DetailActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView txtTitle;
    TextView txtDate;
    TextView txtGenre;
    TextView txtDescription;
    ImageView imgPoster;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTitle = findViewById(R.id.txt_title_detail);
        txtDate = findViewById(R.id.txt_date_detail);
        txtGenre = findViewById(R.id.txt_genre_detail);
        txtDescription = findViewById(R.id.txt_description_detail);
        imgPoster = findViewById(R.id.img_poster_detail);
        progressBar = findViewById(R.id.progress_bar);

        showLoading(true);

        Film parcel = getIntent().getParcelableExtra(CONSTANT.EXTRA_FILM);

        assert parcel != null;
        txtTitle.setText(parcel.getTitle_film());
        txtDate.setText(parcel.getDate_film());
        txtGenre.setText(parcel.getGenre_film());
        txtDescription.setText(parcel.getDescription_film());
        Glide.with(DetailActivity.this)
                .load(CONSTANT.URL_POSTER + parcel.getPoster_film())
                .apply(new RequestOptions().override(100, 150))
                .placeholder(R.drawable.ic_broken_image_black_100dp)
                .into(imgPoster);

        if (!txtTitle.getText().toString().matches("") && !txtDate.getText().toString().matches("") &&
                !txtGenre.getText().toString().matches("") && !txtDescription.getText().toString().matches("")) {
            showLoading(false);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void showLoading(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(DetailActivity.class.getSimpleName(), "showLoading: true");
        } else {
            progressBar.setVisibility(View.GONE);
            Log.d(DetailActivity.class.getSimpleName(), "showLoading: false");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
