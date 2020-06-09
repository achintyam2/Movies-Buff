package com.example.movies.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.adapters.ReviewsAdapter;
import com.example.movies.adapters.TrailersAdapter;
import com.example.movies.database.AppDatabase;
import com.example.movies.models.Movie;
import com.example.movies.models.Review;
import com.example.movies.models.Trailer;
import com.example.movies.utils.Converter;
import com.example.movies.viewmodels.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements TrailersAdapter.TrailerListener {

    private Movie movie = null;
    private ImageView iv_poster, ivFav;
    private TextView tv_title, tv_overview, tv_release_date, tv_rating;
    private MovieDetailViewModel viewModel;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        iv_poster = findViewById(R.id.iv_poster);
        tv_title = findViewById(R.id.tv_title);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_rating = findViewById(R.id.tv_rating);
        tv_overview = findViewById(R.id.tv_overview);
        ivFav = findViewById(R.id.ivFav);
        RecyclerView rvTrailers = findViewById(R.id.rvTrailers);
        rvTrailers.setNestedScrollingEnabled(true);
        RecyclerView rvReviews = findViewById(R.id.rvReviews);
        rvReviews.setNestedScrollingEnabled(true);
        trailersAdapter = new TrailersAdapter(this);
        reviewsAdapter = new ReviewsAdapter();
        rvTrailers.setAdapter(trailersAdapter);
        rvReviews.setAdapter(reviewsAdapter);

        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.updateFav(movie);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }
        movie = intent.getParcelableExtra(Converter.MOVIE_DETAILS);
        if (movie == null) {
            closeOnError();
            return;
        }
        setUpViewModel();
        populateUI(movie);
    }

    private void setUpViewModel() {
        MovieDetailViewModel.Factory factory = new MovieDetailViewModel.Factory(
                getApplication(), AppDatabase.getInstance(getApplication()), movie.getId());
        viewModel = new ViewModelProvider(this, factory).get(MovieDetailViewModel.class);

        viewModel.getIsFav().observe(MovieDetailActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean fav) {
                if (fav) {
                    ivFav.setImageResource(R.drawable.ic_favorite_selected);
                } else {
                    viewModel.fetchTrailers(movie.getId());
                    viewModel.fetchReviews(movie.getId());
                    ivFav.setImageResource(R.drawable.ic_favorite_unselected);
                }
            }
        });

        viewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                viewModel.getTrailers().removeObserver(this);
                trailersAdapter.setTrailers(trailers);
            }
        });

        viewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                viewModel.getReviews().removeObserver(this);
                reviewsAdapter.setReviews(reviews);
            }
        });

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movieObject) {
        Picasso.get().load("http://image.tmdb.org/t/p/w185".concat(movieObject.getPoster())).into(iv_poster);
        tv_title.setText(movieObject.getTitle());
        tv_release_date.setText(movieObject.getRelease_date());
        tv_rating.setText(movieObject.getRating().concat(getResources().getString(R.string.out_of_10)));
        tv_overview.setText(movieObject.getOverview());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("id", movie.getId());
        outState.putString("poster", movie.getPoster());
        outState.putString("title", movie.getTitle());
        outState.putString("release_date", movie.getRelease_date());
        outState.putString("rating", movie.getRating());
        outState.putString("overview", movie.getOverview());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        populateUI(new Movie(
                savedInstanceState.getLong("id"),
                savedInstanceState.getString("title"),
                savedInstanceState.getString("poster"),
                savedInstanceState.getString("overview"),
                savedInstanceState.getString("release_date"),
                savedInstanceState.getString("rating")));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //listener for home
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerClick(Trailer trailer) {
        openVideo(trailer.getKey());
    }

    private void openVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
           startActivity(webIntent);
        }
    }

}
