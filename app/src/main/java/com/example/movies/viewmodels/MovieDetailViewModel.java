package com.example.movies.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.movies.database.AppDatabase;
import com.example.movies.database.AppExecutors;
import com.example.movies.models.Movie;
import com.example.movies.models.Review;
import com.example.movies.models.Trailer;
import com.example.movies.utils.Converter;
import com.example.movies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MovieDetailViewModel extends AndroidViewModel {

    private AppDatabase database;
    private MutableLiveData<Boolean> isFav = new MutableLiveData<>();
    private MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();
    private MutableLiveData<List<Review>> reviews = new MutableLiveData<>();

    public MutableLiveData<List<Trailer>> getTrailers() {
        return trailers;
    }

    public MutableLiveData<List<Review>> getReviews() {
        return reviews;
    }

    public MovieDetailViewModel(@NonNull Application application, AppDatabase appDatabase, final long id) {
        super(application);
        database = appDatabase;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Movie movie = database.movieDao().loadMovieById(id);
                if (movie != null) {
                    trailers.postValue(database.movieDao().loadTrailers(movie.getId()));
                    reviews.postValue(database.movieDao().loadReviews(movie.getId()));
                    isFav.postValue(true);
                } else {
                    isFav.postValue(false);
                }
            }
        });

    }

    public MutableLiveData<Boolean> getIsFav() {
        return isFav;
    }

    public void updateFav(final Movie movie){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isFav.getValue() != null) {
                    if (isFav.getValue()) {
                        database.movieDao().deleteMovie(movie);
                        database.movieDao().deleteTrailers(movie.getId());
                        database.movieDao().deleteReviews(movie.getId());
                        isFav.postValue(false);
                    } else {
                        database.movieDao().insertMovie(movie);
                        insertTrailers(trailers.getValue());
                        insertReviews(reviews.getValue());
                        isFav.postValue(true);
                    }
                }
            }
        });
    }

    private void insertTrailers(final List<Trailer> trailers){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.movieDao().insertTrailers(trailers);
            }
        });
    }


    private void insertReviews(final List<Review> reviews){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.movieDao().insertReviews(reviews);
            }
        });
    }

    public void fetchTrailers(long movieId){
        new FetchTrailerTask().execute(NetworkUtils.getTrailerEndPoint(movieId));
    }

    public void fetchReviews(long movieId){
        new FetchReviewTask().execute(NetworkUtils.getReviewsEndPoint(movieId));
    }

    public class FetchTrailerTask extends AsyncTask<String, Void, List<Trailer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String endPoint = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(endPoint);
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                return Converter.getTrailersDataFromJson(jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Trailer> trailersList) {
            trailers.setValue(trailersList);
        }
    }

    public class FetchReviewTask extends AsyncTask<String, Void, List<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Review> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String endPoint = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(endPoint);
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                return Converter.getReviewsDataFromJson(jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Review> reviewsList) {
            reviews.setValue(reviewsList);
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        @NonNull
        private final AppDatabase mAppDatabase;
        private final long id;

        public Factory(@NonNull Application application,@NonNull AppDatabase appDatabase,
                       long id1) {
            mApplication = application;
            mAppDatabase = appDatabase;
            id = id1;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MovieDetailViewModel(mApplication,mAppDatabase,id);
        }
    }
}
