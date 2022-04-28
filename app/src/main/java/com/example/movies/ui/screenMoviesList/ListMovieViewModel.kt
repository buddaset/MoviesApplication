package com.example.movies.ui.screenMoviesList


import androidx.lifecycle.*
import com.example.movies.data.MovieRepository
import com.example.movies.models.MovieData
import kotlinx.coroutines.launch


class ListMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {



   private val _listMovie = MutableLiveData<List<MovieData>>()
    val listMovie : LiveData<List<MovieData>> = _listMovie

    init {
        getListMovie()
    }

    private fun getListMovie()  {
        viewModelScope.launch {
            _listMovie.value= movieRepository.getListMovie().value
        }
    }

}