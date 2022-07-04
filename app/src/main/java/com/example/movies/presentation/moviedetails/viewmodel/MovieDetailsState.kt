package com.example.movies.presentation.moviedetails.viewmodel

import com.example.movies.domain.model.Movie
import com.example.movies.domain.model.MovieDetails

sealed class MovieDetailsState {

    object Loading : MovieDetailsState()
    data class Error(val e: Throwable) : MovieDetailsState()
    data class Success(val data: MovieDetails) : MovieDetailsState()


}