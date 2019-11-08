package com.dicoding.filmfinal.ui.favorite.movie;

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
import com.dicoding.filmfinal.db.room.MovieFavorite;

import java.util.ArrayList;
import java.util.List;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.ViewHolder> {
    private Context context;
    private List<MovieFavorite> mData = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;

    void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    MovieFavoriteAdapter(Context context) {
        this.context = context;
    }

    void setData(List<MovieFavorite> mData) {
        this.mData.clear();
        this.mData.addAll(mData);

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_favorite, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDate, txtDescription;
        ImageView imgPoster;
        Button btnDeleteFavorite;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDate = itemView.findViewById(R.id.txt_date);
            imgPoster = itemView.findViewById(R.id.img_poster);
            txtDescription = itemView.findViewById(R.id.txt_description);
            btnDeleteFavorite = itemView.findViewById(R.id.btn_delete_favorite);
        }

        void bind(MovieFavorite movieFavorite) {
            txtTitle.setText(movieFavorite.getTitle_movie());
            txtDescription.setText(movieFavorite.getDescription_movie());
            txtDate.setText(movieFavorite.getDate_movie());
            Glide.with(context)
                    .load(CONSTANT.URL_POSTER + movieFavorite.getPoster_movie())
                    .apply(new RequestOptions().override(150, 100))
                    .placeholder(R.drawable.ic_broken_image_black_100dp)
                    .into(imgPoster);
            itemView.setOnClickListener(view -> onItemClickCallback.onItemClicked(movieFavorite, "item"));
            btnDeleteFavorite.setOnClickListener(view -> {
                onItemClickCallback.onItemClicked(movieFavorite, "del");
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(MovieFavorite data, String btn);
    }
}
