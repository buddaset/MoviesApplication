package com.example.movies.ui.screen_movieslist


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.MovieRepository
import com.example.movies.models.MovieData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ListMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {



   private var _listMovie = MutableStateFlow<PagingData<MovieData>>(PagingData.empty())
    val listMovie : StateFlow<PagingData<MovieData>> = _listMovie

    init {
      getListMoviePopular()
    }

    private fun getListMoviePopular() {
        viewModelScope.launch {

            movieRepository.getPagedMovies()
                .cachedIn(viewModelScope)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily,
                    initialValue = PagingData.empty()
                )
                .collect {
                    _listMovie.value = it
                }
        }
}

    fun tryAgain(){
        Log.d("tryAgain" ,"viewmodel")
        _listMovie.value = PagingData.empty()
        getListMoviePopular()

    }

}