package com.example.movies.data.moviedetails.local

import com.example.movies.data.moviedetails.local.dao.ActorDao
import com.example.movies.data.moviedetails.local.dao.MovieDetailsDao
import com.example.movies.data.moviedetails.local.model.ActorEntity
import com.example.movies.data.moviedetails.local.model.MovieDetailsEntity
import com.example.movies.data.moviedetails.local.model.MovieDetailsWithActorsEntity
import kotlinx.coroutines.flow.Flow

interface MovieDetailsLocalDataSource {


    fun getMovieDetailsWithActors(movieId: Long): Flow<MovieDetailsWithActorsEntity>

    fun getActorsMovie(movieId: Long): Flow<List<ActorEntity>>

     suspend fun updateMovieDetails(movie:MovieDetailsEntity)

     suspend fun updateActors(actors: List<ActorEntity>)
}

class MovieDetailsLocalDataSourceImpl(
    private val actorDao: ActorDao,
    private val movieDetailsDao: MovieDetailsDao
) : MovieDetailsLocalDataSource {

    override fun getMovieDetailsWithActors(movieId: Long): Flow<MovieDetailsWithActorsEntity> =
        movieDetailsDao.getMovieById(movieId)

    override fun getActorsMovie(movieId: Long): Flow<List<ActorEntity>> =
        actorDao.getActorsByMovieId(movieId)

    override suspend fun updateMovieDetails(movie: MovieDetailsEntity) =
        movieDetailsDao.insertMovie(movie)

    override suspend fun updateActors(actors: List<ActorEntity>) =
        actorDao.insertAllActors(actors)

}




