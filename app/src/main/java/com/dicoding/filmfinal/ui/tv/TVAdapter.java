package com.dicoding.filmfinal.ui.tv;

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
import com.dicoding.filmfinal.db.models.ResultTV;

import java.util.ArrayList;
import java.util.List;

public class TVAdapter extends RecyclerView.Adapter<TVAdapter.TVViewHolder> {
    private Context context;
    private List<ResultTV> mData = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;

    void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    TVAdapter(Context context) {
        this.context = context;
    }

    void setData(List<ResultTV> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TVAdapter.TVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_film, parent, false);
        return new TVViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull TVAdapter.TVViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TVViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDate, txtDescription;
        ImageView imgPoster;
        Button btnSetFavorite;

        TVViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDate = itemView.findViewById(R.id.txt_date);
            imgPoster = itemView.findViewById(R.id.img_poster);
            txtDescription = itemView.findViewById(R.id.txt_description);
            btnSetFavorite = itemView.findViewById(R.id.btn_set_favorite);
        }

        void bind(final ResultTV resultTV) {
            txtTitle.setText(resultTV.getName());
            txtDescription.setText(resultTV.getOverview());
            txtDate.setText(resultTV.getFirstAirDate());
            Glide.with(context)
                    .load(CONSTANT.URL_POSTER + resultTV.getPosterPath())
                    .apply(new RequestOptions().override(150, 100))
                    .placeholder(R.drawable.ic_broken_image_black_100dp)
                    .into(imgPoster);

            itemView.setOnClickListener(view -> onItemClickCallback.onItemClicked(resultTV, "item"));
            btnSetFavorite.setOnClickListener(view -> onItemClickCallback.onItemClicked(resultTV, "btn"));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ResultTV data, String btn);
    }
}
