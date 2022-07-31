package com.example.movies.presentation.movies.view.movieAdapter

import com.example.movies.domain.model.Movie
import com.example.movies.presentation.core.model.MovieUI

interface MovieClickListener {

    fun onClickMovie(movieUI: MovieUI)

    fun onClickFavorite(movieUI: MovieUI)
}