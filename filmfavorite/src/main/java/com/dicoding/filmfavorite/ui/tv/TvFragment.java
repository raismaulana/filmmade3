package com.dicoding.filmfavorite.ui.tv;

import android.content.Context;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.filmfavorite.R;
import com.dicoding.filmfavorite.db.DatabaseContract;
import com.dicoding.filmfavorite.entity.TVFavorite;
import com.dicoding.filmfavorite.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TvFragment extends Fragment implements LoadTvCallback {

    private TvAdapter tvAdapter;
    private ProgressBar progressBar;
    private TextView tvNoData;

    public TvFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        tvNoData = view.findViewById(R.id.txt_no_data);
        tvNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView rvTv = view.findViewById(R.id.rv_tv);
        tvAdapter = new TvAdapter(getActivity());
        rvTv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTv.setHasFixedSize(true);
        rvTv.setAdapter(tvAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity());
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(DatabaseContract.TvColumns.CONTENT_URI_TV
                , true, myObserver);
        new LoadTvAsyncTask(getActivity(), this).execute();
    }

    @Override
    public void postExecute(List<TVFavorite> tvFavorites) {
        progressBar.setVisibility(View.GONE);
        if (tvFavorites.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            tvAdapter.setData(tvFavorites);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            tvAdapter.setData(new ArrayList<>());
        }
    }


    private static class LoadTvAsyncTask extends AsyncTask<Void, Void, List<TVFavorite>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTvCallback> weakCallback;


        private LoadTvAsyncTask(Context context, LoadTvCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected List<TVFavorite> doInBackground(Void... voids) {
            return MappingHelper.mapCursorTvToArrayList(
                    Objects.requireNonNull(weakContext.get().getContentResolver()
                            .query(DatabaseContract.TvColumns.CONTENT_URI_TV
                            , null, null, null, null))
            );
        }

        @Override
        protected void onPostExecute(List<TVFavorite> data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }

    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadTvAsyncTask(context, (LoadTvCallback) context).execute();
        }
    }
}

interface LoadTvCallback {
    void postExecute(List<TVFavorite> tvFavorite);
}