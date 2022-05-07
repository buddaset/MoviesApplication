package com.example.movies.ui.screen_movieslist


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.MovieRepository
import com.example.movies.models.MovieData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ListMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val searchBy = MutableLiveData("")

    private var _listMovie = MutableStateFlow<PagingData<MovieData>>(PagingData.empty())
    val listMovie: StateFlow<PagingData<MovieData>> = _listMovie

    init {
        getSearchMovie()
    }

    fun setSearchBy(value: String) {
        if (this.searchBy.value == value) return
        this.searchBy.value = value
        getSearchMovie()
    }

    private fun getSearchMovie() {
        viewModelScope.launch {

            searchBy.asFlow()
                .debounce(500)
                .flatMapLatest {
                    movieRepository.searchMovie(it)
                }
                .cachedIn(viewModelScope)
                .collectLatest {
                    _listMovie.value = it
                }

        }


    }



    fun tryAgain() {
        _listMovie.value = PagingData.empty()
        getSearchMovie()


    }


}