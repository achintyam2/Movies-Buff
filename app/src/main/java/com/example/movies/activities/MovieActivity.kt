package com.example.movies.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.example.movies.R
import com.example.movies.adapters.MoviesAdapter
import com.example.movies.adapters.MoviesPagedAdapter
import com.example.movies.models.Movie
import com.example.movies.utils.GridAutofitLayoutManager
import com.example.movies.viewmodels.MovieViewModel
import com.pt.soch.newsapp.utils.getViewModel
import com.pt.soch.newsapp.utils.gone
import com.pt.soch.newsapp.utils.visible
import kotlinx.android.synthetic.main.activity_movies.*
import java.util.*

class MovieActivity : AppCompatActivity() {

    private lateinit var viewModel : MovieViewModel
    private var favSelected = false
    private val moviesPagedAdapter  by lazy { MoviesPagedAdapter() }
    private val moviesAdapter  by lazy { MoviesAdapter(this) }
    private val favMovies: ArrayList<Movie> = ArrayList()
    private val movieObserver = Observer<PagedList<Movie>>  {
        if (!favSelected)
        if (it.size > 0) {
            showRv()
            moviesPagedAdapter.submitList(it)
        }else{
            hideRv()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        setContentView(R.layout.activity_movies)
        val gridLayoutManager = GridAutofitLayoutManager(this, 500)
        rv_movies?.layoutManager = gridLayoutManager
        rv_movies?.setHasFixedSize(true)

        savedInstanceState?.let {
            favSelected = it.getBoolean("favSelected")
        }
        viewModel.getPagedList().observe(this, movieObserver)

        viewModel.getFavMovies().observe(this, Observer {
            favMovies.clear()
            favMovies.addAll(it)
            if (favSelected){
                progress_bar?.gone()
                tv_message?.gone()
                rv_movies?.visible()
                title = resources.getString(R.string.favourites)
                moviesAdapter.setMovieData(null)
                moviesAdapter.setMovieData(it)
                rv_movies?.adapter = moviesAdapter
            }
        })



    }

    private fun showRv(){
        moviesPagedAdapter.setActivityInstance(this@MovieActivity)
        rv_movies?.adapter = moviesPagedAdapter
        progress_bar?.gone()
        tv_message?.gone()
        rv_movies?.visible()
    }

    private fun hideRv(){
        progress_bar?.gone()
        rv_movies?.gone()
        tv_message?.visible()
        tv_message?.setOnClickListener {
            viewModel.getPagedList().observe(this@MovieActivity, movieObserver)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.sort, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_most_popular) {
            title = resources.getString(R.string.pop_movies)
            progress_bar?.visible()
            favSelected = false
            moviesAdapter.setMovieData(null)
            viewModel.getPagedList().observe(this,movieObserver)
            return true
        }

        if (id == R.id.action_favourites) {
            title = resources.getString(R.string.favourites)
            moviesAdapter.setMovieData(null)
            favSelected = true
            setUpFavs()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpFavs(){
        rv_movies?.adapter = moviesAdapter
        moviesAdapter.setMovieData(favMovies)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("favSelected",favSelected)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        favSelected = savedInstanceState.getBoolean("favSelected")
        super.onRestoreInstanceState(savedInstanceState)
    }
}