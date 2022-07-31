package com.example.movies.presentation.favorite_movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.model.Movie
import com.example.movies.domain.usecase.ChangeFavoriteFlagMovieUseCase
import com.example.movies.domain.usecase.GetFavoriteMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoriteMoviesViewModel(getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
private val changeFavoriteFlagMovieUseCase: ChangeFavoriteFlagMovieUseCase) : ViewModel() {


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

    private fun handleEmptyOrNotListMovies(movies: List<Movie>) : FavoriteMoviesState =
        if (movies.isEmpty()) FavoriteMoviesState.Empty
    else FavoriteMoviesState.Success(data = movies)


    fun changeFavoriteFlagMovie(movie: Movie) = viewModelScope.launch {
         // change favorite flag movie
        changeFavoriteFlagMovieUseCase(movieId = movie.id, !movie.isLiked) // change favorite flag movies
    }

}


sealed interface FavoriteMoviesState {

    object Loading : FavoriteMoviesState
    data class Error(val e: Throwable) : FavoriteMoviesState
    data class Success(val data: List<Movie>) : FavoriteMoviesState
    object Empty : FavoriteMoviesState
}

class FavoriteMoviesViewModelFactory(private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
private val changeFavoriteFlagMovieUseCase: ChangeFavoriteFlagMovieUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        FavoriteMoviesViewModel(getFavoriteMoviesUseCase, changeFavoriteFlagMovieUseCase ) as T


}