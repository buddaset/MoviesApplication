package com.example.movies.domain.repository

import androidx.paging.PagingData
import com.example.movies.core.util.Result
import com.example.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getPopularMovies() : Flow<PagingData<Movie>>

    fun getMoviesBySearch(query: String) :  Flow<PagingData<Movie>>

    fun getFavoriteMovies() : Flow<List<Movie>>

    fun getFavoriteIds(): Flow<List<Long>>

    fun periodicalBackgroundUpdateMovie()

    suspend fun changeFavoriteFlagMovie(movieId: Long, isFavorite: Boolean)
}