package com.example.movies.data.remote

import com.example.movies.core.util.Result
import com.example.movies.core.util.runOperationCatching
import com.example.movies.data.remote.model.ActorDto
import com.example.movies.data.remote.model.GenreDto
import com.example.movies.data.remote.model.MovieDetailsDto
import com.example.movies.data.remote.model.MovieDto


interface MoviesRemoteDataSource {

    suspend fun searchMovies(query: String): Result<List<MovieDto>, Throwable>

    suspend fun loadPopularMovies(pageIndex: Int, pageSize: Int): Result<List<MovieDto>, Throwable>

    suspend fun loadMovieDetails(movieId: Int): Result<MovieDetailsDto, Throwable>

    suspend fun  loadGenres(): Result<List<GenreDto>, Throwable>

    suspend fun loadMovieActors(movieId: Int) : Result<List<ActorDto>, Throwable>


}

class MoviesRemoteDataSourceImpl(
    private val movieApi: MovieApi
) : MoviesRemoteDataSource {

    override suspend fun searchMovies(query: String): Result<List<MovieDto>, Throwable> =
        runOperationCatching {
            movieApi.searchMovie(query = query).results
        }


    override suspend fun loadPopularMovies(
        pageIndex: Int, pageSize: Int
    ): Result<List<MovieDto>, Throwable> = runOperationCatching {

        movieApi.loadMoviesPopular(page = pageIndex, pageSize = pageSize).results
    }

    override suspend fun loadMovieDetails(movieId: Int): Result<MovieDetailsDto, Throwable> =
        runOperationCatching {
            movieApi.loadMovieDetails(movieId)
        }

    override suspend fun loadGenres(): Result<List<GenreDto>, Throwable> = runOperationCatching {
        movieApi.loadGenres().genres
    }

    override suspend fun loadMovieActors(movieId: Int): Result<List<ActorDto>, Throwable> =
        runOperationCatching {
        movieApi.loadMovieActors(movieId).actors
    }


}