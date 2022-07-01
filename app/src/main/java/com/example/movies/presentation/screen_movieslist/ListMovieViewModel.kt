package com.example.movies.presentation.screen_movieslist


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.domain.repository.MovieRepository
import com.example.movies.domain.model.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*


class ListMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val searchBy = MutableLiveData("")

    val listMovie: Flow<PagingData<Movie>> = getSearchMovie()

    init {
        movieRepository.periodicalBackgroundUpdateMovie()
    }


    fun setSearchBy(query: String) {
        if (this.searchBy.value == query) return
        this.searchBy.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    private fun getSearchMovie(): Flow<PagingData<Movie>> =
        searchBy.asFlow()
            .debounce(500)
            .flatMapLatest { movieRepository.getMoviesBySearch(it) }
            .cachedIn(viewModelScope)


}




