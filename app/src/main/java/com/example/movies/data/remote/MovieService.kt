package com.example.movies.data.remote

import com.example.movies.data.remote.response.*
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {


    @GET("configuration")
    suspend fun loadConfiguration() : ConfigurationResponse

    @GET("movie/popular")
    suspend fun loadMoviesPopular() : PopularMoviesResponse

    @GET("genre/movie/list")
    suspend fun loadGenres(): GenresResponse

    @GET("movie/{movie_id}")
    suspend fun loadMovieDetails(@Path("movie_id") movieId: Int) : MovieDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun loadMovieCredits(@Path("movie_id") movieId: Int) : ActorsResponse
}