package com.example.movies.data.movies.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.movies.local.model.GenreEntityDb


@Dao
interface GenreDao {

    @Query("SELECT * FROM genres")
fun getAllGenres(): List<GenreEntityDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<GenreEntityDb>)

    @Query("DELETE FROM genres")
    suspend fun clearAll()

}