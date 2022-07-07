package com.example.movies.presentation.movies.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.domain.model.Movie
import com.example.movies.domain.usecase.GetMoviesBySearchUseCase
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest


class MoviesViewModel(
    private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val searchBy = MutableLiveData("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val movies: Flow<PagingData<Movie>> = searchBy
        .asFlow()
        .debounce(TEXT_ENTERED_DEBOUNCE_MILLIS)
        .flatMapLatest { query ->
            if (query.isEmpty()) getPopularMoviesUseCase()  // use default
            else getMoviesBySearchUseCase(query)
        }
        .distinctUntilChanged()
        .cachedIn(viewModelScope)

    fun setSearchBy(query: String) {
        if (searchBy.value == query) return
        searchBy.value = query
    }

    companion object {

        private const val TEXT_ENTERED_DEBOUNCE_MILLIS = 500L
    }

}



