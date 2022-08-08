package com.example.movies.presentation.moviedetails.model

import com.example.movies.domain.model.MovieDetails
import com.example.movies.presentation.core.util.formatGenres
import com.example.movies.presentation.core.util.formatRating

data class MovieDetailsUI(val details: MovieDetails) {


    val genres :  String
        get() =  formatGenres(details.genres)

    val rating: Float
        get() =  formatRating(details.rating)
}

