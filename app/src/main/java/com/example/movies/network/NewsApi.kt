package com.example.movies.network

import com.example.movies.models.MoviesResponse
import retrofit2.http.*

interface NewsApi {

    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey : String,
        @Query("page") page : Int
    ): MoviesResponse

    @GET("top_rated")
    suspend fun getTopMovies(
       @Query("api_key") apiKey : String,
       @Query("page") page : Int
    ): MoviesResponse
}