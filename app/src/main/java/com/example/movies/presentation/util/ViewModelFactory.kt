package com.example.movies.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.domain.repository.MoviesRepository
import com.example.movies.presentation.moviedetails.viewmodel.DetailsMovieViewModel

class ViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
    when(modelClass) {
// todo        MoviesViewModel::class.java -> MoviesViewModel()
        DetailsMovieViewModel::class.java -> DetailsMovieViewModel(repository)
        else -> throw IllegalArgumentException("Unknown viewModel")
    }    as T

}