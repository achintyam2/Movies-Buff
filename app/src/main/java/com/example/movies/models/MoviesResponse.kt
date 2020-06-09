package com.example.movies.models

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
        @field:SerializedName("page")
        val page: Int,
        @field:SerializedName("results")
        val results : List<Movie> = arrayListOf()
)