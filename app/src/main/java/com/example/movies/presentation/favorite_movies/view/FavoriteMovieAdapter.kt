package com.example.movies.presentation.favorite_movies.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.presentation.core.model.MovieUI
import com.example.movies.presentation.movies.view.movieAdapter.MovieCallback
import com.example.movies.presentation.movies.view.movieAdapter.MovieClickListener
import com.example.movies.presentation.movies.view.movieAdapter.MovieViewHolder

class FavoriteMovieAdapter(private val clickListener: MovieClickListener) :
    ListAdapter<MovieUI, MovieViewHolder>(MovieCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(ViewHolderMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieUI = getItem(position)
        holder.bind(movieUI, clickListener)
    }
}