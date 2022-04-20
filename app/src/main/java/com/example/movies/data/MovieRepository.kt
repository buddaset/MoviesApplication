package com.example.movies.data

import androidx.lifecycle.LiveData
import com.example.movies.models.MovieData

interface MovieRepository {

    suspend fun getListMovie() : LiveData<List<MovieData>>

    suspend fun getMovieDetails(idMovie: Int): LiveData<MovieData>
}