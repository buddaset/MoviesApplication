package com.example.movies.data.moviedetails.local.dao

import androidx.room.*
import com.example.movies.data.moviedetails.local.model.MovieDetailsEntity
import com.example.movies.data.moviedetails.local.model.MovieDetailsWithActorsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDetailsDao {


    @Transaction
    @Query("SELECT * FROM movies_detail WHERE id = :id")
     fun getMovieById(id: Long) : Flow<MovieDetailsWithActorsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetailsEntity)
}