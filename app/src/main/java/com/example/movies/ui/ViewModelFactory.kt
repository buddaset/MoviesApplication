package com.example.movies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.data.MovieRepository
import com.example.movies.ui.screen_moviedetails.DetailsMovieViewModel
import com.example.movies.ui.screen_movieslist.ListMovieViewModel

class ViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
    when(modelClass) {
         ListMovieViewModel::class.java -> ListMovieViewModel(repository)
        DetailsMovieViewModel::class.java -> DetailsMovieViewModel(repository)
        else -> throw IllegalArgumentException("Unknown viewModel")
    }    as T

}