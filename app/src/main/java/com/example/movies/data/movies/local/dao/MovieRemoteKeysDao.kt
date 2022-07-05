package com.example.movies.data.movies.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.movies.local.model.MovieRemoteKeys

@Dao
interface MovieRemoteKeysDao {

    @Query("SELECT * FROM movie_remote_keys WHERE movies = :movies")
    suspend fun getRemoteKey(movies: String): MovieRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: MovieRemoteKeys)

    @Query("DELETE FROM movie_remote_keys  WHERE movies = :movies")
    suspend fun delete(movies: String)

}