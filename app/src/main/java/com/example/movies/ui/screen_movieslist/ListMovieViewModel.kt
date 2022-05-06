package com.example.movies.ui.screen_movieslist


import android.util.Log
import androidx.lifecycle.*
import com.example.movies.data.MovieRepository
import com.example.movies.models.MovieData
import kotlinx.coroutines.launch
import com.example.movies.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ListMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {



   private val _listMovie = MutableStateFlow<Result<List<MovieData>>>(Result.Loading())
    val listMovie : StateFlow<Result<List<MovieData>>> = _listMovie

    init {
        getListMovie()
    }

    private fun getListMovie()  {
        viewModelScope.launch {
            Log.d("Try" , " Try again")
            movieRepository.getListMovies().collect {
                _listMovie.value = it
            }
        }
    }


    fun tryAgain(){
        _listMovie.value = Result.Loading()
        getListMovie()
    }

}