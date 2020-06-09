package com.example.movies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.movies.models.Movie;
import com.example.movies.models.Review;
import com.example.movies.models.Trailer;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailers(List<Trailer> trailers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReviews(List<Review> reviews);

    @Query("DELETE FROM trailers WHERE id = :id")
    void deleteTrailers(long id);

    @Query("DELETE FROM reviews WHERE id = :id")
    void deleteReviews(long id);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movie WHERE id = :id")
    Movie loadMovieById(long id);

    @Query("SELECT * FROM trailers WHERE id = :id")
    List<Trailer> loadTrailers(long id);

    @Query("SELECT * FROM reviews WHERE id = :id")
    List<Review> loadReviews(long id);
}
