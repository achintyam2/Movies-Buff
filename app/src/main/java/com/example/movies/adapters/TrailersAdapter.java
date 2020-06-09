package com.example.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.models.Trailer;

import java.util.List;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MoviesAdapterViewHolder>{

    private TrailerListener trailerListener;
    private List<Trailer> trailers;

    public TrailersAdapter(TrailerListener trailerListener){
        this.trailerListener = trailerListener;
    }

    public void setTrailers(List<Trailer> trailers){
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public interface TrailerListener{
        void onTrailerClick(Trailer trailer);
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_trailers;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        Trailer trailer = trailers.get(holder.getAdapterPosition());
        holder.ivName.setText(trailer.getTrailerName());
    }

    @Override
    public int getItemCount() {
        return trailers != null ? trailers.size() : 0;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView ivName;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            ivName = view.findViewById(R.id.ivName);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (trailerListener != null)
                trailerListener.onTrailerClick(trailers.get(getAdapterPosition()));
        }

    }

}
