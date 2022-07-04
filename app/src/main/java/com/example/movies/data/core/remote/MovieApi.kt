package com.example.movies.data.core.remote

import androidx.annotation.IntRange
import com.example.movies.data.moviedetails.remote.model.MovieDetailsDto
import com.example.movies.data.core.remote.response.ActorsResponse
import com.example.movies.data.core.remote.response.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {


    @GET("configuration")
    suspend fun loadConfiguration(): ConfigurationResponse

    @GET("search/movie")
    suspend fun searchMovie (
        @Query("query") query: String? = null,
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("pageSize") @IntRange(from = 1, to = MAX_PAGE_SIZE.toLong()) pageSize: Int = DEFAULT_PAGE_SIZE,
    ) : MoviesResponse

    @GET("movie/popular")
    suspend fun loadMoviesPopular(
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("pageSize") @IntRange(from = 1, to = MAX_PAGE_SIZE.toLong()) pageSize: Int = DEFAULT_PAGE_SIZE
    ): MoviesResponse

    @GET("genre/movie/list")
    suspend fun loadGenres(): GenresResponse

    @GET("movie/{movie_id}")
    suspend fun loadMovieDetails(@Path("movie_id") movieId: Long): MovieDetailsDto

    @GET("movie/{movie_id}/credits")
    suspend fun loadMovieActors(@Path("movie_id") movieId: Long): ActorsResponse


    companion object {

        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 20
    }

}