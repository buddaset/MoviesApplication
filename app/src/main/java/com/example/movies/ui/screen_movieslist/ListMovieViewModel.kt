package com.example.movies.ui.screen_movieslist


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.MovieRepository
import com.example.movies.models.MovieData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*


class ListMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val searchBy = MutableLiveData("")

    val listMovie: Flow<PagingData<MovieData>> = getSearchMovie()

    init {
        movieRepository.periodicalBackgroundUpdateMovie()
    }


    fun setSearchBy(value: String) {
        if (this.searchBy.value == value) return
        this.searchBy.value = value
    }

    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    private fun getSearchMovie(): Flow<PagingData<MovieData>> =
        searchBy.asFlow()
            .debounce(500)
            .flatMapLatest { movieRepository.searchMovie(it) }
            .cachedIn(viewModelScope)


}




