package com.example.movies.domain.repository

import androidx.paging.PagingData
import com.example.movies.core.util.Result
import com.example.movies.domain.model.Actor
import com.example.movies.domain.model.Movie
import com.example.movies.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {





    fun getPopularMovies() : Flow<PagingData<Movie>>

    suspend fun getMoviesBySearch(query: String) : Result<List<Movie>, Throwable>

    suspend fun getMovieDetails(movieId: Int): Flow<MovieDetails>

    suspend fun getActorsMovie(movieId: Int) : Flow<List<Actor>>

    fun periodicalBackgroundUpdateMovie()
}