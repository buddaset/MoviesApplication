package com.example.movies.data.workers

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import androidx.work.*

import com.example.movies.data.core.local.MovieDatabase

import com.example.movies.data.movies.local.model.MovieRemoteKeys
import com.example.movies.data.core.remote.MovieApi
import com.example.movies.data.core.remote.MovieApi.Companion.MAX_PAGE_SIZE
import com.example.movies.core.util.toEntity
import com.example.movies.data.movies.local.model.MovieEntityDb
import java.lang.Exception
import java.util.concurrent.TimeUnit


class RefreshMoviesWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val movieDatabase: MovieDatabase,
    private val movieApi: MovieApi
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
                    movieApi.loadMoviesPopular(page = i).results.map { it.toEntity(genres, "test") } // todo  change baseUrl
                movies.addAll(moviesEntityDb)


            }
            Log.d("Worker", "movies  --- $movies")
            return Result.success()
        } catch (ex: Exception) {
            Log.d("Worker", "exception")
            return Result.failure()
        }
    }

    companion object {
        private const val START_PAGE = 1
        private const val REFRESH_WORKER_TAG ="RefreshMovieWorkerTag"
        const val WORKER_NAME = "refreshMovieWorker"

//        fun makeOneTimeRequest(): OneTimeWorkRequest  =
//            OneTimeWorkRequestBuilder<RefreshMoviesWorker>()
//                .setInitialDelay(40,TimeUnit.SECONDS)
//                .build()

        fun makePeriodicWorkRequest() : PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(RefreshMoviesWorker::class.java, 8 ,TimeUnit.HOURS)
                .addTag(REFRESH_WORKER_TAG)
                .setConstraints(constraints())
                .build()


        private fun constraints(): Constraints =
            Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
    }
}