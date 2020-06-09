package com.example.movies.utils;

import com.example.movies.R;
import com.example.movies.models.Movie;
import com.example.movies.models.Review;
import com.example.movies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_DETAILS = "movie_details";


    public static List<Movie> getMoviesDataFromJson(String json){
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0;i < results.length() ; i++){
                String poster = POSTER_BASE_URL+results.getJSONObject(i).getString("poster_path");
                String title = results.getJSONObject(i).getString("title");
                String release_date = results.getJSONObject(i).getString("release_date");
                Double vote_average = results.getJSONObject(i).getDouble("vote_average");
                String overview = results.getJSONObject(i).getString("overview");
                long id = results.getJSONObject(i).getLong("id");
                Movie movie = new Movie(id,title,poster,overview,release_date,String.valueOf(vote_average));
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static List<Trailer> getTrailersDataFromJson(String json){
        List<Trailer> trailers = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            long movieId = jsonObject.getLong("id");
            for (int i = 0 ; i< results.length() ; i++){
                String trailerId = results.getJSONObject(i).getString("id");
                String trailerName = results.getJSONObject(i).getString("name");
                String trailerKey = results.getJSONObject(i).getString("key");
                Trailer trailer = new Trailer(trailerId,movieId,trailerName,trailerKey);
                trailers.add(trailer);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return trailers;
    }

    public static List<Review> getReviewsDataFromJson(String json){
        List<Review> reviews = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            long movieId = jsonObject.getLong("id");
            for (int i = 0 ; i< results.length() ; i++){
                String reviewId = results.getJSONObject(i).getString("id");
                String author = results.getJSONObject(i).getString("author");
                String content = results.getJSONObject(i).getString("content");
                Review review = new Review(reviewId,author,content,movieId);
                reviews.add(review);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return reviews;
    }

}
