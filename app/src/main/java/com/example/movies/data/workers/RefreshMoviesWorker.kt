package com.example.movies.data.workers

import android.content.Context
import androidx.room.withTransaction

import androidx.work.CoroutineWorker

import androidx.work.WorkerParameters
import com.example.movies.data.local.MovieDatabase
import com.example.movies.data.local.entity.MovieEntityDb
import com.example.movies.data.local.entity.MovieRemoteKeys
import com.example.movies.data.remote.MovieService
import com.example.movies.data.remote.MovieService.Companion.MAX_PAGE_SIZE
import com.example.movies.data.utils.toMovieEntityDb
import java.lang.Exception


class RefreshMoviesWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val movieDatabase: MovieDatabase,
    private val movieService: MovieService
) : CoroutineWorker(context, workerParameters) {

    private val movieDao = movieDatabase.movieDao()
    private val movieRemoteKeysDao = movieDatabase.movieRemoteKeysDao()


    override suspend fun doWork(): Result {

        try {
            val countMovies = movieDao.getCountMovies()
            val countPage = countMovies /  MAX_PAGE_SIZE
            val movies = mutableListOf<MovieEntityDb>()
            val genres = movieDatabase.genreDao().getAllGenres()
            val keys = mutableListOf<MovieRemoteKeys>()
            for (i in START_PAGE..countPage) {
                val moviesEntityDb = movieService.loadMoviesPopular(page = i).results.map { it.toMovieEntityDb(genres) }
                val keysPage = moviesEntityDb.map {
                    MovieRemoteKeys(
                        id = it.id,
                        prevKey = if(i == START_PAGE) null else i - 1,
                        nextKey = if (moviesEntityDb.size < MAX_PAGE_SIZE) null else i + 1
                    )
                }
                movies.addAll(moviesEntityDb)
                keys.addAll(keysPage)

            }
            movieDatabase.withTransaction {
                movieDao.clearAllMovie()
                movieRemoteKeysDao.deleteAllRemoteKeys()
                movieDao.insertAllMovie(movies)
                movieRemoteKeysDao.addAllRemoteKeys(keys)
            }
            return Result.success()
        } catch (ex: Exception) {
            return Result.failure()
        }
    }

    companion object {
        private const val START_PAGE = 1
    }
}