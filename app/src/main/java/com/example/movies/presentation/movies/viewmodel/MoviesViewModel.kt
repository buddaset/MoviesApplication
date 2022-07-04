package com.example.movies.presentation.movies.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.domain.repository.MovieRepository
import com.example.movies.domain.model.Movie
import com.example.movies.domain.usecase.GetMoviesBySearchUseCase
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*


class MoviesViewModel(private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase,
private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModel() {

    private val searchBy = MutableLiveData("")

    val movies: Flow<PagingData<Movie>> = getPopularMoviesUseCase()


}

//    fun setSearchBy(query: String) {
//        if (this.searchBy.value == query) return
//        this.searchBy.value = query
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
//    private fun getSearchMovie(): Flow<PagingData<Movie>> =
//        searchBy.asFlow()
//            .debounce(500)
//            .flatMapLatest { getMoviesBySearchUseCase() }
//            .cachedIn(viewModelScope)
//
//
//}




