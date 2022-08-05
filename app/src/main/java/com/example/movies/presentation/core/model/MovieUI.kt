package com.example.movies.presentation.core.model

import com.example.movies.domain.model.Movie
import com.example.movies.presentation.core.util.formatGenres
import com.example.movies.presentation.core.util.formatRating

data class MovieUI (
    val movie: Movie,
    val isFavorite: Boolean
    ) {
    val genres :  String
        get() =  formatGenres(movie.genres)

    val rating: Float
        get() =  formatRating(movie.rating)

}


