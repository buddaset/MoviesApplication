package com.example.movies.data.movies.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.movies.data.movies.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE isPopular = :isPopular")
    fun getPopularMovies(isPopular:Boolean = true): PagingSource<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieEntity)

    @Transaction
    @Query("DELETE FROM movies WHERE movies.id NOT IN (SELECT movie_id FROM favorites)")
    suspend fun clearAllMovie()

    @Query("SELECT COUNT('id') FROM movies")
    suspend fun getCountMovies(): Int

    @Transaction
    @Query("SELECT * FROM movies WHERE movies.id IN (SELECT movie_id FROM favorites)")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>
}