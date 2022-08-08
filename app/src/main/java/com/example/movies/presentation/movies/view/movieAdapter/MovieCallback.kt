package com.example.movies.presentation.movies.view.movieAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.movies.domain.model.Movie
import com.example.movies.presentation.core.model.MovieUI

class MovieCallback: DiffUtil.ItemCallback<MovieUI>() {

    override fun areItemsTheSame(oldData: MovieUI, newData: MovieUI): Boolean =
        oldData.movie.id == newData.movie.id

    override fun areContentsTheSame(oldData: MovieUI, newData: MovieUI): Boolean =
        oldData == newData

}