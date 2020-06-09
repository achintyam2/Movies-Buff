package com.example.movies.utils

import androidx.paging.DataSource
import com.example.movies.models.Movie

class MoviesDataFactory : DataSource.Factory<Int, Movie> (){

    override fun create(): DataSource<Int, Movie> {
        return MovieDataSource()
    }
}