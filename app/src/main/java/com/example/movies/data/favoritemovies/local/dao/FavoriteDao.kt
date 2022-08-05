package com.example.movies.data.favoritemovies.local.dao

import androidx.room.*
import com.example.movies.data.favoritemovies.local.model.FavoriteIdEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {

    @Query("SELECT movie_id FROM favorites")
    fun getFavoriteIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteMovie: FavoriteIdEntity)

    @Delete
    suspend fun delete(favoriteMovie: FavoriteIdEntity)
}