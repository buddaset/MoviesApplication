package com.example.movies.presentation.core.model

import com.example.movies.domain.model.Movie

data class MovieUI (
    val movie: Movie,
    val isFavorite: Boolean
    )