package com.example.movies.presentation.movies.view.movieAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.movies.domain.model.Movie

 class MovieCallback: DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldData: Movie, newData: Movie): Boolean =
        oldData.id == newData.id

    override fun areContentsTheSame(oldData: Movie, newData: Movie): Boolean =
        oldData == newData

}