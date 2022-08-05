package com.example.movies.data.moviedetails.remote

import com.example.movies.data.core.util.Result
import com.example.movies.data.core.util.runOperationCatching
import com.example.movies.data.core.remote.MovieApi
import com.example.movies.data.moviedetails.remote.model.ActorDto
import com.example.movies.data.moviedetails.remote.model.MovieDetailsDto

interface MovieDetailsRemoteDataSource {

    suspend fun loadMovieDetails(movieId: Long): Result<MovieDetailsDto, Throwable>

    suspend fun loadMovieActors(movieId: Long) : Result<List<ActorDto>, Throwable>

}

class MovieDetailsRemoteDataSourceImpl(private val movieApi: MovieApi): MovieDetailsRemoteDataSource {

    override suspend fun loadMovieDetails(movieId: Long): Result<MovieDetailsDto, Throwable> =
        runOperationCatching {
            movieApi.loadMovieDetails(movieId)
        }

    override suspend fun loadMovieActors(movieId: Long): Result<List<ActorDto>, Throwable> =
        runOperationCatching {
            movieApi.loadMovieActors(movieId).actors
        }

}