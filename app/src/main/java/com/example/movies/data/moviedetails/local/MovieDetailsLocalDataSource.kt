package com.example.movies.data.moviedetails.local

import com.example.movies.data.moviedetails.local.dao.ActorDao
import com.example.movies.data.moviedetails.local.dao.MovieDetailsDao
import com.example.movies.data.moviedetails.local.model.ActorEntityDb
import com.example.movies.data.moviedetails.local.model.MovieDetailsEntityDb
import com.example.movies.data.moviedetails.local.model.MovieDetailsWithActorsEntity
import com.example.movies.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieDetailsLocalDataSource {


    fun getMovieDetailsWithActors(movieId: Long): Flow<MovieDetailsWithActorsEntity>

    fun getActorsMovie(movieId: Long): Flow<List<ActorEntityDb>>

     suspend fun updateMovieDetails(movie:MovieDetailsEntityDb)

     suspend fun updateActors(actors: List<ActorEntityDb>)
}

class MovieDetailsLocalDataSourceImpl(
    private val actorDao: ActorDao,
    private val movieDetailsDao: MovieDetailsDao
) : MovieDetailsLocalDataSource {

    override fun getMovieDetailsWithActors(movieId: Long): Flow<MovieDetailsWithActorsEntity> =
        movieDetailsDao.getMovieById(movieId)

    override fun getActorsMovie(movieId: Long): Flow<List<ActorEntityDb>> =
        actorDao.getActorsByMovieId(movieId)

    override suspend fun updateMovieDetails(movie: MovieDetailsEntityDb) =
        movieDetailsDao.insertMovie(movie)

    override suspend fun updateActors(actors: List<ActorEntityDb>) =
        actorDao.insertAllActors(actors)

}




