package com.example.movies.presentation.movies.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.movies.domain.model.Movie
import com.example.movies.domain.usecase.ChangeFavoriteFlagMovieUseCase
import com.example.movies.domain.usecase.GetFavoriteMovieIdsUseCase
import com.example.movies.domain.usecase.GetMoviesBySearchUseCase
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import com.example.movies.presentation.core.model.MovieUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class MoviesViewModel(
    private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val changeFavoriteFlagMovieUseCase: ChangeFavoriteFlagMovieUseCase,
    private val getFavoriteMovieIdsUseCase: GetFavoriteMovieIdsUseCase
) : ViewModel() {

    private val searchBy = MutableStateFlow(DEFAULT_QUERY)
    
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val pagingMovies: Flow<PagingData<Movie>> = searchBy
        .debounce(TEXT_ENTERED_DEBOUNCE_MILLIS)
        .flatMapLatest { choiceSourceFlow(it)}
        .distinctUntilChanged()
        .cachedIn(viewModelScope)

    val movies : Flow<PagingData<MovieUI>> =
        combine(pagingMovies, getFavoriteMovieIdsUseCase(), ::merge )



    fun setSearchBy(query: String) {
        if (searchBy.value == query) return
        searchBy.value = query // todo state flow distict value check
    }

    fun changeFavoriteFlagMovie(movieUI: MovieUI) = viewModelScope.launch {
        val newFlag = !movieUI.isFavorite
        changeFavoriteFlagMovieUseCase(movieUI.movie, newFlag)
    }

    private fun merge(pagingData: PagingData<Movie>, favoriteIds: List<Long>) : PagingData<MovieUI> =
        pagingData.map { movie ->
            MovieUI(
                movie = movie,
                isFavorite = favoriteIds.contains(movie.id)
            )
        }



    private fun choiceSourceFlow(query: String) : Flow<PagingData<Movie>>  =
        if (query.isEmpty()) getPopularMoviesUseCase() else getMoviesBySearchUseCase(query)


    companion object {
        private const val DEFAULT_QUERY = ""
        private const val TEXT_ENTERED_DEBOUNCE_MILLIS = 500L
    }

}



