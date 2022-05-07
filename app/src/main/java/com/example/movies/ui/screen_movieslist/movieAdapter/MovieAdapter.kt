package com.example.movies.ui.screen_movieslist.movieAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.models.MovieData

class MovieAdapter(private var listener: MovieListener) : PagingDataAdapter<MovieData, MovieDataViewHolder>(MovieCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDataViewHolder =
        MovieDataViewHolder(ViewHolderMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieDataViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            listener.onCLickMovie(movie)
        }
    }
}

interface MovieListener {

    fun onCLickMovie(movie: MovieData)
}