package com.dicoding.filmfinal.ui.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.filmfinal.CONSTANT;
import com.dicoding.filmfinal.R;
import com.dicoding.filmfinal.db.models.ResultMovie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private List<ResultMovie> mData = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;

    void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    MovieAdapter(Context context) {
        this.context = context;
    }

    void setData(List<ResultMovie> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_film, viewGroup, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        movieViewHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDate, txtDescription;
        ImageView imgPoster;
        Button btnSetFavorite;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDate = itemView.findViewById(R.id.txt_date);
            imgPoster = itemView.findViewById(R.id.img_poster);
            txtDescription = itemView.findViewById(R.id.txt_description);
            btnSetFavorite = itemView.findViewById(R.id.btn_set_favorite);
        }

        void bind(final ResultMovie resultMovie) {
            txtTitle.setText(resultMovie.getTitle());
            txtDescription.setText(resultMovie.getOverview());
            txtDate.setText(resultMovie.getReleaseDate());
            Glide.with(context)
                    .load(CONSTANT.URL_POSTER + resultMovie.getPosterPath())
                    .apply(new RequestOptions().override(150, 100))
                    .placeholder(R.drawable.ic_broken_image_black_100dp)
                    .into(imgPoster);

            itemView.setOnClickListener(view -> onItemClickCallback.onItemClicked(resultMovie, "item"));
            btnSetFavorite.setOnClickListener(view -> onItemClickCallback.onItemClicked(resultMovie, "btn"));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ResultMovie data, String btn);
    }
}
