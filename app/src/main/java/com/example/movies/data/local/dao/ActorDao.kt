package com.example.movies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.local.entity.ActorEntityDb


@Dao
interface ActorDao {

    @Query("SELECT * FROM actors WHERE movie_id = :idMovie")
    suspend fun getActorsByMovieId(idMovie: Int): List<ActorEntityDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllActors(list: List<ActorEntityDb>)



}