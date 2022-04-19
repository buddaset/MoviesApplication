package com.example.movies.ui.screenMoviesList


import android.app.Application
import androidx.lifecycle.*
import com.android.academy.fundamentals.homework.features.data.loadMovies
import com.example.movies.models.MovieData
import kotlinx.coroutines.launch


class ListMovieViewModel(application: Application) : AndroidViewModel(application) {



   private val _listMovie = MutableLiveData<List<MovieData>>()
    val listMovie : LiveData<List<MovieData>> = _listMovie

    init {
        getListMovie()
    }

    private fun getListMovie() {
        viewModelScope.launch {
            val list = loadMovies(getApplication())
            _listMovie.value = list

        }
    }

}