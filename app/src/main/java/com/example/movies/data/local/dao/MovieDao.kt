package com.example.movies.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.local.entity.MovieEntityDb


@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE title LIKE :query ORDER BY title ASC")
    fun getAllMovies(query: String) : PagingSource<Int, MovieEntityDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovie(list: List<MovieEntityDb>)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovie()
}