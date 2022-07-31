package com.example.movies.presentation.favorite_movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.model.Movie
import com.example.movies.domain.usecase.ChangeFavoriteFlagMovieUseCase
import com.example.movies.domain.usecase.GetFavoriteMoviesUseCase
import com.example.movies.presentation.core.model.MovieUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoriteMoviesViewModel(
    getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val changeFavoriteFlagMovieUseCase: ChangeFavoriteFlagMovieUseCase
) : ViewModel() {


    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteMovies: StateFlow<FavoriteMoviesState> =
        getFavoriteMoviesUseCase()
            .catch { error -> FavoriteMoviesState.Error(error) }

            .mapLatest { movies -> handleEmptyOrNotListMovies(movies) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FavoriteMoviesState.Loading
            )

    private fun handleEmptyOrNotListMovies(movies: List<Movie>): FavoriteMoviesState =
        if (movies.isEmpty()) FavoriteMoviesState.Empty
        else FavoriteMoviesState.Success( data =
        movies.map { MovieUI(movie = it , isFavorite = true) })  // only isFavorite movies



    fun changeFavoriteFlagMovie(movieUI: MovieUI) = viewModelScope.launch {
        val newFlag = !movieUI.isFavorite
        changeFavoriteFlagMovieUseCase(movieUI.movie.id, newFlag)

    }

}



sealed interface FavoriteMoviesState {

    object Loading : FavoriteMoviesState
    data class Error(val e: Throwable) : FavoriteMoviesState
    data class Success(val data: List<MovieUI>) : FavoriteMoviesState
    object Empty : FavoriteMoviesState
}

class FavoriteMoviesViewModelFactory(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val changeFavoriteFlagMovieUseCase: ChangeFavoriteFlagMovieUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        FavoriteMoviesViewModel(getFavoriteMoviesUseCase, changeFavoriteFlagMovieUseCase) as T


}