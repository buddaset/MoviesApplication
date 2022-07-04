package com.example.movies.data.moviedetails.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.moviedetails.local.model.MovieDetailsEntityDb
import com.example.movies.data.moviedetails.local.model.MovieDetailsWithActorsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDetailsDao {


    @Query("SELECT * FROM movies_detail WHERE id = :id")
     fun getMovieById(id: Long) : Flow<MovieDetailsWithActorsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetailsEntityDb)
}