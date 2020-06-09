package com.example.movies.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.activities.MovieDetailActivity
import com.example.movies.models.Movie
import com.example.movies.utils.Converter
import com.pt.soch.newsapp.utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_movie.view.*

class MoviesPagedAdapter :
        PagedListAdapter<Movie, RecyclerView.ViewHolder>(DiffCallback()) {

    lateinit var activity: Activity
    var onMovieSelected = {movie : Movie,position: Int -> Unit}

    class DiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            if (oldItem is Movie && newItem is Movie) {
                return oldItem == newItem
            }
            return false
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            if (oldItem is Movie && newItem is Movie) {
                return oldItem.poster == newItem.poster
            }
            return false
        }
    }

    fun setActivityInstance(act: Activity){
        this.activity = act
    }

    class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(parent.inflate(R.layout.list_item_movie))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)
        when(holder){
            is MoviesViewHolder -> {
                holder.apply {
                    itemView.apply {
                        Picasso.get().load("http://image.tmdb.org/t/p/w185".plus(movie?.poster)).into(iv_movie)
                        setOnClickListener { movie?.let { it1 ->
                            val intent = Intent(activity, MovieDetailActivity::class.java)
                            intent.putExtra(Converter.MOVIE_DETAILS,movie)
                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                 val options = ActivityOptions
                                        .makeSceneTransitionAnimation(activity,iv_movie, "poster")
                                 activity.startActivity(intent,options.toBundle())
                            } else {
                                activity.startActivity(intent)
                            }
                        }
                        }
                    }
                }
            }
        }

    }

    fun getListItem(position: Int): Movie? {
        return getItem(position)
    }
}