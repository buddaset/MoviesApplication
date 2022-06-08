package com.example.movies.data

import androidx.paging.PagingData
import com.example.movies.models.ActorData
import com.example.movies.models.MovieData
import com.example.movies.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {



    fun getMovieDetails(idMovie: Int): Flow<Result<MovieDetails>>

    fun getActorsMovie(idMovie: Int) : Flow<Result<List<ActorData>>>


    fun searchMovie(query: String) : Flow<PagingData<MovieData>>

    fun periodicalBackgroundUpdateMovie()
}