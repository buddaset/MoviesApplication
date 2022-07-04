package com.example.movies.presentation.moviedetails.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.usecase.GetMovieDetailsUseCase
import com.example.movies.presentation.moviedetails.view.MovieDetailsFragment.Companion.MOVIE_ID
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailsMovieViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Long = savedStateHandle.get<Long>(MOVIE_ID) ?: error("Unknown movie Id")

    private val _movie = MutableStateFlow<MovieDetailsState>(MovieDetailsState.Loading)
    val movie: StateFlow<MovieDetailsState> = _movie.asStateFlow()

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









