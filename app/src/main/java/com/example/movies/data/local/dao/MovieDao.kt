package com.example.movies.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.local.entity.MovieEntityDb


@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE :query =  '' OR title LIKE '%' || :query || '%' " )
    fun getMovies(query: String) : PagingSource<Int, MovieEntityDb>

    @Query("SELECT * FROM movies ")
    fun getPopularMovies(): PagingSource<Int, MovieEntityDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovie(list: List<MovieEntityDb>)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovie()

    @Query("SELECT COUNT('id') FROM movies")
    suspend fun getCountMovies() : Int
}