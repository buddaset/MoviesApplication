package com.example.movies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.local.entity.MovieDetailsEntityDb


@Dao
interface MovieDetailsDao {


    @Query("SELECT * FROM movies_detail WHERE id = :id")
    suspend fun getMovieById(id: Int) : MovieDetailsEntityDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetailsEntityDb)
}