package com.example.movies.data.movies.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.movies.local.model.MovieRemoteKeys

@Dao
interface MovieRemoteKeysDao {

    @Query("SELECT * FROM movie_remote_keys WHERE movie_id = :movieId")
    suspend fun getRemoteKeysByMovieId(movieId: Long): MovieRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<MovieRemoteKeys>)

    @Query("DELETE FROM movie_remote_keys")
    suspend fun delete()

}