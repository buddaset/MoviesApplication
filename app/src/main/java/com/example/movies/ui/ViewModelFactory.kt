package com.example.movies.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.data.MovieRepository
import com.example.movies.ui.screenDetailsMovie.DetailsMovieViewModel
import com.example.movies.ui.screenMoviesList.ListMovieViewModel

class ViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
    when(modelClass) {
         ListMovieViewModel::class.java -> ListMovieViewModel(repository)
        DetailsMovieViewModel::class.java -> DetailsMovieViewModel(repository)
        else -> throw IllegalArgumentException("Unknown viewModel")
    }    as T

}