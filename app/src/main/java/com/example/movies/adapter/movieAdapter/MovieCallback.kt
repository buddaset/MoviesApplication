package com.example.movies.adapter.movieAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.movies.models.MovieItem

class MovieCallback: DiffUtil.ItemCallback<MovieItem>() {
    override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem == newItem
    }
}