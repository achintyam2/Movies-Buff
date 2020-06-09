package com.example.movies.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movies.database.AppDatabase
import com.example.movies.models.Movie
import com.example.movies.utils.MoviesDataFactory

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private var favMovies: LiveData<List<Movie>>

//    private var favMovies = MutableLiveData<List<Movie>>()

    init {
        val appDatabase = AppDatabase.getInstance(getApplication())
        favMovies = appDatabase.movieDao().loadAllMovies()
    }

    fun getPagedList(): LiveData<PagedList<Movie>> {
        return LivePagedListBuilder(MoviesDataFactory(), pagedListConfig()).build()
    }

    fun getFavMovies(): LiveData<List<Movie>> {
        return favMovies
    }

    fun getFavouriteMovies(){
        val appDatabase = AppDatabase.getInstance(getApplication())
        favMovies= appDatabase.movieDao().loadAllMovies()
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
            .setInitialLoadSizeHint(5)
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .setPrefetchDistance(3)
            .build()
}