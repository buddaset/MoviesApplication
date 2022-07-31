package com.example.movies.presentation.movies.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.domain.model.Movie
import com.example.movies.domain.usecase.ChangeFavoriteFlagMovieUseCase
import com.example.movies.domain.usecase.GetMoviesBySearchUseCase
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class MoviesViewModel(
    private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val changeFavoriteFlagMovieUseCase: ChangeFavoriteFlagMovieUseCase
) : ViewModel() {

    private val searchBy = MutableStateFlow(DEFAULT_QUERY)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val movies: Flow<PagingData<Movie>> = searchBy
        .debounce(TEXT_ENTERED_DEBOUNCE_MILLIS)
        .flatMapLatest { choiceSourceFlow(it)}
        .distinctUntilChanged()
        .cachedIn(viewModelScope)

    fun setSearchBy(query: String) {
        if (searchBy.value == query) return
        searchBy.value = query
    }

    fun changeFavoriteFlagMovie(movie: Movie) = viewModelScope.launch {
        val newMovie = movie.copy(isLiked = !movie.isLiked) // change favorite flag movie
        changeFavoriteFlagMovieUseCase(newMovie)
    }


    private fun choiceSourceFlow(query: String) : Flow<PagingData<Movie>>  =
        if (query.isEmpty()) getPopularMoviesUseCase() else getMoviesBySearchUseCase(query)


    companion object {
        private const val DEFAULT_QUERY = ""
        private const val TEXT_ENTERED_DEBOUNCE_MILLIS = 500L
    }

}



