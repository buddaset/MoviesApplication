package com.example.movies.ui.screenDetailsMovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MovieRepository
import com.example.movies.models.MovieDetails
import kotlinx.coroutines.launch

class DetailsMovieViewModel(private val repository: MovieRepository): ViewModel() {



    fun getMovieDetail(idMovie: Int) : LiveData<MovieDetails> {
        val result = MutableLiveData<MovieDetails>()
        viewModelScope.launch {
            val movie = repository.getMovieDetails(idMovie)
            result.value = movie.value
        }
        return result
    }
}