package com.example.movies.presentation.moviedetails.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.example.movies.domain.usecase.GetMovieDetailsUseCase
import com.example.movies.presentation.moviedetails.view.MovieDetailsFragment.Companion.MOVIE_ID
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class DetailsMovieViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Long = savedStateHandle.get<Long>(MOVIE_ID) ?: error("Unknown movie Id")

    private val _movie = MutableStateFlow<MovieDetailsState>(MovieDetailsState.Loading)
    val movie: StateFlow<MovieDetailsState> = _movie.asStateFlow()

    init {
        getMovieDetail()
    }

    private fun getMovieDetail() {
        viewModelScope.launch {
            getMovieDetailsUseCase(movieId)
                .catch { error -> _movie.value = MovieDetailsState.Error(error) }
                .collectLatest { movie -> _movie.value = MovieDetailsState.Success(movie) }
        }
    }

    fun tryAgain() {
        _movie.value = MovieDetailsState.Loading
        getMovieDetail()
    }

}






class DetailViewModelFactory(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = DetailsMovieViewModel(getMovieDetailsUseCase, handle) as T

}









