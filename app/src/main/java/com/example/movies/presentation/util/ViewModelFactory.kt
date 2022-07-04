package com.example.movies.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.domain.repository.MovieRepository
import com.example.movies.presentation.screen_moviedetails.DetailsMovieViewModel
import com.example.movies.presentation.movies.viewmodel.MoviesViewModel

class ViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
    when(modelClass) {
// todo        MoviesViewModel::class.java -> MoviesViewModel()
        DetailsMovieViewModel::class.java -> DetailsMovieViewModel(repository)
        else -> throw IllegalArgumentException("Unknown viewModel")
    }    as T

}