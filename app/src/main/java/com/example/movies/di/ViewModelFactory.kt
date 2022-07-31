package com.example.movies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.domain.usecase.ChangeFavoriteFlagMovieUseCase
import com.example.movies.domain.usecase.GetMoviesBySearchUseCase
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import com.example.movies.presentation.movies.viewmodel.MoviesViewModel


class ViewModelFactory(private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase,
private val changeFavoriteFlagMovieUseCase: ChangeFavoriteFlagMovieUseCase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass) {
            MoviesViewModel::class.java -> MoviesViewModel(getMoviesBySearchUseCase, getPopularMoviesUseCase, changeFavoriteFlagMovieUseCase)
            else -> throw IllegalArgumentException("Unknown viewModel")
        }    as T

}