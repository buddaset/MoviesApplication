package com.example.movies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.local.entity.MovieDetailsEntityDb
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDetailsDao {


    @Query("SELECT * FROM movies_detail WHERE id = :id")
     fun getMovieById(id: Int) : Flow<MovieDetailsEntityDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetailsEntityDb)
}