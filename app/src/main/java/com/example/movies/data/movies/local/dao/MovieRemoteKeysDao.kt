package com.example.movies.data.movies.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.movies.local.model.MovieRemoteKeys

@Dao
interface MovieRemoteKeysDao {

    @Query("SELECT * FROM movie_remote_keys WHERE id = :id")
    suspend fun getRemoteKey(id: Long): MovieRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<MovieRemoteKeys>)

    @Query("DELETE FROM movie_remote_keys")
    suspend fun deleteAllRemoteKeys()

}