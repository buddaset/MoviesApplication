package com.example.movies.data.workers

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import androidx.work.*

import com.example.movies.data.local.MovieDatabase
import com.example.movies.data.local.entity.MovieEntityDb
import com.example.movies.data.local.entity.MovieRemoteKeys
import com.example.movies.data.remote.MovieService
import com.example.movies.data.remote.MovieService.Companion.MAX_PAGE_SIZE
import com.example.movies.data.utils.toMovieEntityDb
import java.lang.Exception
import java.util.concurrent.TimeUnit


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
            Log.d("Worker", "start worker")
            val countMovies = movieDao.getCountMovies()
            val countPage = countMovies / MAX_PAGE_SIZE
            val movies = mutableListOf<MovieEntityDb>()
            val genres = movieDatabase.genreDao().getAllGenres()
            val keys = mutableListOf<MovieRemoteKeys>()
            for (i in START_PAGE..countPage) {
                Log.d("Worker", "start loader")
                val moviesEntityDb =
                    movieService.loadMoviesPopular(page = i).results.map { it.toMovieEntityDb(genres) }
                val keysPage = moviesEntityDb.map {
                    MovieRemoteKeys(
                        id = it.id,
                        prevKey = if (i == START_PAGE) null else i - 1,
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
            Log.d("Worker", "movies  --- $movies")
            return Result.success()
        } catch (ex: Exception) {
            Log.d("Worker", "exeption")
            return Result.failure()
        }
    }

    companion object {
        private const val START_PAGE = 1
        private const val REFRESH_WORKER_TAG ="RefreshMovieWorkerTag"
        const val WORKER_NAME = "refreshMovieWorker"

        fun makePeriodicWorkRequest() : PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(RefreshMoviesWorker::class.java, 15 ,TimeUnit.MINUTES, 2, TimeUnit.MINUTES)
                .addTag(REFRESH_WORKER_TAG)
//                .setConstraints(constraints())
                .build()


        private fun constraints(): Constraints =
            Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
    }
}