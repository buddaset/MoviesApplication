package com.example.movies.presentation.favorite_movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.model.Movie
import com.example.movies.domain.model.MovieDetails
import com.example.movies.domain.usecase.GetFavoriteMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class FavoriteMoviesViewModel(getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase) : ViewModel() {


    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteMovies: StateFlow<FavoriteMoviesState<List<Movie>>> =
        getFavoriteMoviesUseCase()
            .catch { error -> FavoriteMoviesState.Error(error) }
            .mapLatest { movies -> FavoriteMoviesState.Success(movies) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FavoriteMoviesState.Loading()
            )

}


sealed interface FavoriteMoviesState<T> {

    class Loading<T> : FavoriteMoviesState<T>
    data class Error<T>(val e: T) : FavoriteMoviesState<T>
    data class Success<T>(val data: T) : FavoriteMoviesState<T>
}