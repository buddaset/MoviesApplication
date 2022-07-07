package com.example.movies.data.movies.remote

import android.util.Log
import com.example.movies.core.util.Result
import com.example.movies.core.util.runOperationCatching
import com.example.movies.data.core.remote.MovieApi
import com.example.movies.data.movies.remote.model.GenreDto
import com.example.movies.data.movies.remote.model.MovieDto


interface MoviesRemoteDataSource {

    suspend fun searchMovies(query: String, pageIndex: Int): Result<List<MovieDto>, Throwable>

    suspend fun loadPopularMovies(pageIndex: Int, pageSize: Int): Result<List<MovieDto>, Throwable>

    suspend fun  loadGenres(): Result<List<GenreDto>, Throwable>
}


class MoviesRemoteDataSourceImpl(
    private val movieApi: MovieApi
) : MoviesRemoteDataSource {

    override suspend fun searchMovies(query: String, pageIndex: Int = 1): Result<List<MovieDto>, Throwable> =
        runOperationCatching {
            movieApi.searchMovie(query = query).results
        }

    override suspend fun loadPopularMovies(
        pageIndex: Int, pageSize: Int
    ): Result<List<MovieDto>, Throwable> =

     runOperationCatching {

        Log.d("MoviesRepositoryImpl", "remoteSourse")
       movieApi.loadMoviesPopular(page = pageIndex, pageSize = pageSize).results


    }

    override suspend fun loadGenres(): Result<List<GenreDto>, Throwable> = runOperationCatching {
        movieApi.loadGenres().genres
    }

}