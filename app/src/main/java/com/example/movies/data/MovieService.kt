package com.example.movies.data

import retrofit2.http.GET

interface MovieService {


    @GET("")
    fun getMoviesPopular()
}