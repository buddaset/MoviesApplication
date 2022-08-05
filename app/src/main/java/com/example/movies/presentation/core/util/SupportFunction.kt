package com.example.movies.presentation.core.util

import com.example.movies.domain.model.Genre


private const val MAX_RATING = 5

fun formatRating(rating: Int) : Float =
    if (rating > MAX_RATING) (rating % MAX_RATING).toFloat()
    else rating.toFloat()

fun formatGenres(genres: List<Genre>): String =
    genres.joinToString { it.name }




