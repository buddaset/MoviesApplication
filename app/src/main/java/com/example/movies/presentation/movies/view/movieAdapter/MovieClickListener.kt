package com.example.movies.presentation.movies.view.movieAdapter

import com.example.movies.domain.model.Movie

interface MovieClickListener {

    fun onClickMovie(movie: Movie)

    fun onClickFavorite(movie: Movie)
}