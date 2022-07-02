package com.example.movies.domain.repository

import androidx.paging.PagingData
import com.example.movies.data.Result
import com.example.movies.domain.model.Actor
import com.example.movies.domain.model.Movie
import com.example.movies.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {



    fun getMovieDetails(idMovie: Int): Flow<Result<MovieDetails, Throwable>>

    fun getPopularMovies() : Flow<PagingData<Movie>>

    fun getActorsMovie(idMovie: Int) : Flow<Result<List<Actor>,Throwable>>


    fun getMoviesBySearch(query: String) : Flow<PagingData<Movie>>

    fun periodicalBackgroundUpdateMovie()
}