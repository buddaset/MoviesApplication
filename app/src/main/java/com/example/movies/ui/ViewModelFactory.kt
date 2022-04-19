package com.example.movies.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.ui.screenMoviesList.ListMovieViewModel

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
    when(modelClass) {
         ListMovieViewModel::class.java -> ListMovieViewModel(application)
        else -> throw IllegalArgumentException("Unknown viewModel")
    }    as T

}