package com.example.movies.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.movies.models.ActorData
import com.example.movies.models.MovieData
import com.example.movies.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {


    suspend fun getMovieDetails(idMovie: Int): Flow<Result<MovieDetails>>

    suspend fun getActorsMovie(idMovie: Int) : Flow<Result<List<ActorData>>>

    fun getPagedMovies(): Flow<PagingData<MovieData>>
}