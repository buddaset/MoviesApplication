package com.example.movies.ui.screenMoviesList.movieAdapter

import com.example.movies.models.GenreData

object MovieUtils {


    private const val MAX_RATING = 5

    fun getGenreOfMovie(listGenre: List<GenreData>) : String =
        listGenre.joinToString { it.name }

    fun getRating(rating : Int): Float =
        if (rating > MAX_RATING) (rating % MAX_RATING).toFloat()
        else rating.toFloat()



}