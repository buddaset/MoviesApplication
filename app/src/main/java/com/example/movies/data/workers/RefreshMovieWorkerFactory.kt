package com.example.movies.data.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.movies.data.local.MovieDatabase
import com.example.movies.data.remote.MovieService

class RefreshMovieWorkerFactory(
    private val movieDatabase: MovieDatabase,
    private val movieService: MovieService
) : WorkerFactory() {


    override fun createWorker(
        appContext: Context, workerClassName: String, workerParameters: WorkerParameters
    ): ListenableWorker? {
        return RefreshMoviesWorker(appContext, workerParameters, movieDatabase, movieService)
    }

}