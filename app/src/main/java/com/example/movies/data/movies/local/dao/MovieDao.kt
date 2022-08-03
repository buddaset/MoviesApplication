package com.example.movies.data.movies.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.movies.data.movies.local.model.MovieEntityDb
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {

//    @Query("SELECT * FROM movies WHERE :query =  '' OR title LIKE '%' || :query || '%' " )
//    fun getMovies(query: String) : PagingSource<Int, MovieEntityDb>

    @Query("SELECT * FROM movies")
    fun getPopularMovies(): PagingSource<Int, MovieEntityDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<MovieEntityDb>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieEntityDb)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovie()

    @Query("SELECT COUNT('id') FROM movies")
    suspend fun getCountMovies() : Int

    @Transaction
    @Query("SELECT * FROM movies WHERE movies.id IN (SELECT movie_id FROM favorites)")
    fun getFavoriteMovies(): Flow<List<MovieEntityDb>>
}