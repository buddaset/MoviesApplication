package com.example.movies.ui.screen_moviedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MovieRepository
import com.example.movies.models.MovieDetails
import kotlinx.coroutines.launch
import com.example.movies.data.Result
import com.example.movies.models.ActorData
import kotlinx.coroutines.flow.*

class DetailsMovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movie = MutableStateFlow<Result<MovieDetails>>(Result.Loading())
    private val _actorsMovie = MutableStateFlow<Result<List<ActorData>>>(Result.Loading())

    val movieDetails: StateFlow<Result<MovieDetails>> = combine(
        _movie,
        _actorsMovie,
        ::mergeSource
    ).stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000L), initialValue = Result.Loading())


    fun getMovieDetail(idMovie: Int) {
        viewModelScope.launch {

            repository.getMovieDetails(idMovie).collect {
                _movie.value = it
            }

            repository.getActorsMovie(idMovie).collect {
                _actorsMovie.value = it
            }

        }
    }


    private fun mergeSource(
        movie: Result<MovieDetails>,
        actors: Result<List<ActorData>>
    ): Result<MovieDetails> =

        when (movie) {
            is Result.Success -> {
                val newMovie =movie.data.copy(actors = getResultActors(actors))
                Log.d("AAA", "MERGE \n ${movie.data.actors}  }")
                Result.Success(newMovie)
            }

            else -> movie
        }
}

private fun getResultActors(actors: Result<List<ActorData>>): List<ActorData> {

   return when (actors) {

        is Result.Success -> {
            Log.d("AAA", "actor \n ${actors.data}  }")
            val list = actors.data
            list
        }

        else -> emptyList()
    }
}


