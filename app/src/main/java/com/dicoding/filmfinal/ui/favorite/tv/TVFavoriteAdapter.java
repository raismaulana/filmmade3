package com.dicoding.filmfinal.ui.favorite.tv;

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
import com.dicoding.filmfinal.db.room.TVFavorite;

import java.util.ArrayList;
import java.util.List;

public class TVFavoriteAdapter extends RecyclerView.Adapter<TVFavoriteAdapter.ViewHolder> {
    private Context context;
    private List<TVFavorite> tvFavoriteList = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;

    void setOnItemClickCallback(OnItemClickCallback onitemClickCallback) {
        this.onItemClickCallback = onitemClickCallback;
    }

    TVFavoriteAdapter(Context context) {
        this.context = context;
    }

    void setData(List<TVFavorite> tvFavorites) {
        this.tvFavoriteList.clear();
        this.tvFavoriteList.addAll(tvFavorites);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TVFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tvFavoriteList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvFavoriteList.size();
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

        void bind(TVFavorite tvFavorite) {
            txtTitle.setText(tvFavorite.getTitle_tv());
            txtDescription.setText(tvFavorite.getDescription_tv());
            txtDate.setText(tvFavorite.getDate_tv());
            Glide.with(context)
                    .load(CONSTANT.URL_POSTER + tvFavorite.getPoster_tv())
                    .apply(new RequestOptions().override(150, 100))
                    .placeholder(R.drawable.ic_broken_image_black_100dp)
                    .into(imgPoster);
            itemView.setOnClickListener(view -> onItemClickCallback.onItemClicked(tvFavorite, "item"));
            btnDeleteFavorite.setOnClickListener(view -> {
                onItemClickCallback.onItemClicked(tvFavorite, "del");
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TVFavorite data, String btn);
    }
}
