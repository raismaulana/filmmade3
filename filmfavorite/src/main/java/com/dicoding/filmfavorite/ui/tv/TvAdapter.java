package com.dicoding.filmfavorite.ui.tv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.filmfavorite.CONSTANT;
import com.dicoding.filmfavorite.R;
import com.dicoding.filmfavorite.entity.TVFavorite;

import java.util.ArrayList;
import java.util.List;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.ViewHolder> {

    private Context context;
    private List<TVFavorite> mData = new ArrayList<>();

    TvAdapter(Context context) {
        this.context = context;
    }

    void setData(List<TVFavorite> mData) {
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDate = itemView.findViewById(R.id.txt_date);
            imgPoster = itemView.findViewById(R.id.img_poster);
            txtDescription = itemView.findViewById(R.id.txt_description);
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
        }
    }
}
