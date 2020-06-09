package com.example.movies.utils

import androidx.paging.PageKeyedDataSource
import com.example.movies.models.Movie
import com.example.movies.models.MoviesResponse
import com.example.movies.network.NetworkRepository
import com.example.movies.network.Response

import kotlinx.coroutines.runBlocking

class MovieDataSource : PageKeyedDataSource<Int, Movie>() {

    private val pageNumber = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>) {
        runBlocking {
            val data = NetworkRepository.getPopularMovies(pageNumber)
            val list = handleResponse(data)
            list?.let { callback.onResult(it, pageNumber, pageNumber + 1) }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        runBlocking {
            val data = NetworkRepository.getPopularMovies(params.key)
            val list = handleResponse(data)
            list?.let { callback.onResult(it, params.key + 1) }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

    private fun  handleResponse(
        rssData: Response<MoviesResponse>
    ) : List<Movie>?{
        when(rssData) {
            is Response.Success -> {
                rssData.responseData?.results?.let {
                    return it
                }
            }
        }
        return null
    }

}