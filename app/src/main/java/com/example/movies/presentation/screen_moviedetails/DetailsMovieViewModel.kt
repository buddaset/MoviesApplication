package com.example.movies.presentation.screen_moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.repository.MovieRepository
import com.example.movies.data.Result
import com.example.movies.domain.model.Actor
import com.example.movies.domain.model.MovieDetails
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class DetailsMovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movie = MutableStateFlow<Result<MovieDetails>>(Result.Loading())
    private val _actorsMovie = MutableStateFlow<Result<List<Actor>>>(Result.Loading())
    private var lastMovieId by Delegates.notNull<Int>()

    val movieDetails: StateFlow<Result<MovieDetails>> = combine(
        _movie,
        _actorsMovie,
        ::mergeSource
    ).stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Result.Loading()
    )


    fun getMovieDetail(idMovie: Int) {
        lastMovieId = idMovie

        viewModelScope.launch {

            repository.getMovieDetails(idMovie).collect { _movie.value = it }

            repository.getActorsMovie(idMovie).collect { _actorsMovie.value = it }
        }
    }

    fun tryAgain(){
        _movie.value = Result.Loading()
        getMovieDetail(lastMovieId)
    }


    private fun mergeSource(
        movie: Result<MovieDetails>,
        actors: Result<List<Actor>>
    ): Result<MovieDetails> =

        when (movie) {
            is Result.Success -> {
                val newMovie = movie.data.copy(actors = getResultActors(actors))
                Result.Success(newMovie)
            }

            else -> movie
        }


    private fun getResultActors(actors: Result<List<Actor>>): List<Actor> {

        return when (actors) {

            is Result.Success -> {

                val list = actors.data
                list
            }

            else -> emptyList()
        }
    }


}


