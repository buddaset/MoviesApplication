package com.example.movies.presentation.movies.view.movieAdapter

import com.example.movies.domain.model.Genre

object MovieUtils {


    private const val MAX_RATING = 5

    fun getGenreOfMovie(listGenre: List<Genre>) : String =
        listGenre.joinToString { it.name }

    fun getRating(rating : Int): Float =
        if (rating > MAX_RATING) (rating % MAX_RATING).toFloat()
        else rating.toFloat()



}