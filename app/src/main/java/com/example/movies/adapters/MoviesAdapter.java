package com.example.movies.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.activities.MovieDetailActivity;
import com.example.movies.models.Movie;
import com.example.movies.utils.Converter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{


    private List<Movie> movies;
    private Activity activity;



    public MoviesAdapter(Activity activity){
        this.activity = activity;
    }

    public void setMovieData(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public interface MovieListener{
        void onMovieClick(Movie movie);
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        Movie movie = movies.get(holder.getAdapterPosition());
        Picasso.get().load("http://image.tmdb.org/t/p/w185".concat(movie.getPoster())).into(holder.iv_movie);
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    private Movie getItem(int position){
        return movies.get(position);
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView iv_movie;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            iv_movie = view.findViewById(R.id.iv_movie);
            iv_movie.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, MovieDetailActivity.class);
            intent.putExtra(Converter.MOVIE_DETAILS,getItem(getAdapterPosition()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(activity,iv_movie, "poster");
                activity.startActivity(intent,options.toBundle());
            } else {
                activity.startActivity(intent);
            }
        }

    }

}
