package com.example.movies.presentation.moviedetails.viewmodel


import com.example.movies.presentation.moviedetails.model.MovieDetailsUI

sealed class MovieDetailsState {

    object Loading : MovieDetailsState()
    data class Error(val e: Throwable) : MovieDetailsState()
    data class Success(val data: MovieDetailsUI) : MovieDetailsState()


}