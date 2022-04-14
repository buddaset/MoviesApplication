package com.example.movies.adapter.movieAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.movies.models.MovieData

class MovieCallback: DiffUtil.ItemCallback<MovieData>() {
    override fun areItemsTheSame(oldData: MovieData, newData: MovieData): Boolean {
        return oldData.name == newData.name
    }

    override fun areContentsTheSame(oldData: MovieData, newData: MovieData): Boolean {
        return oldData == newData
    }
}