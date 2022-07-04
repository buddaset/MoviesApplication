package com.example.movies.data.moviedetails.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.moviedetails.local.model.ActorEntityDb
import kotlinx.coroutines.flow.Flow


@Dao
interface ActorDao {

    @Query("SELECT * FROM actors WHERE movie_id = :idMovie")
   fun getActorsByMovieId(idMovie: Long): Flow<List<ActorEntityDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllActors(list: List<ActorEntityDb>)



}