package com.example.movies.data.remote

import com.example.movies.data.remote.response.ConfigurationResponse
import com.example.movies.data.remote.response.MovieDetailsResponse
import com.example.movies.data.remote.response.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {


    @GET("configuration")
    suspend fun loadConfiguration() : ConfigurationResponse

    @GET("movie/popular")
    suspend fun loadMoviesPopular() : PopularMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun loadMovieDetails(@Path("movie_id") movieId: Int) : MovieDetailsResponse
}