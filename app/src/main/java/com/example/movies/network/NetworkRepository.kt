package com.example.movies.network

import com.example.movies.models.MoviesResponse
import com.example.movies.network.AppConstants.API_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkRepository {

    private val responseHandler: ResponseHandler by lazy {
        ResponseHandler()
    }

    //creating a Network Interceptor to add api_key in all the request as authInterceptor
    private val interceptor = Interceptor { chain ->
        val url = chain.request().url().newBuilder().addQueryParameter("apiKey", "API_KEY").build()
        val request = chain.request()
            .newBuilder()
            .build()
        chain.proceed(request)
    }
    // we are creating a networking client using OkHttp and add our authInterceptor.
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .addInterceptor(logging)
        .build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(apiClient)
            .build()
    }

    private val newsApi = getRetrofit()
        .create(NewsApi::class.java)




    suspend fun getPopularMovies(page: Int): Response<MoviesResponse> {
        return try {
            responseHandler.handleSuccess(newsApi.getPopularMovies(API_KEY,page), AppConstants.POPULAR_MOVIES)
        } catch (e: Exception) {
            responseHandler.handleException(e, AppConstants.POPULAR_MOVIES)
        }
    }

    suspend fun getTopMovies(page: Int): Response<MoviesResponse> {
        return try {
            responseHandler.handleSuccess(newsApi.getTopMovies(API_KEY,page), AppConstants.TOP_MOVIES)
        } catch (e: Exception) {
            responseHandler.handleException(e, AppConstants.TOP_MOVIES)
        }
    }

}