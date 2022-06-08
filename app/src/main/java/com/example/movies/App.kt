package com.example.movies

import android.app.Application
import androidx.work.Configuration
import com.example.movies.data.MovieRepository
import com.example.movies.data.repository.MovieRepositoryImpl
import com.example.movies.data.dispatchers.IoDispatcher
import com.example.movies.data.local.MovieDatabase
import com.example.movies.data.remote.MovieService
import com.example.movies.data.utils.ImageUrlAppender
import com.example.movies.data.workers.RefreshMovieWorkerFactory
import com.example.movies.di.DatabaseModule
import com.example.movies.di.NetworkModule
import kotlinx.coroutines.Dispatchers

class App : Application(), Configuration.Provider {

    lateinit var repository: MovieRepository
    private val dispatcher = IoDispatcher(value = Dispatchers.IO)
    private lateinit var movieDatabase: MovieDatabase
    private lateinit var service: MovieService

    override fun onCreate() {
        super.onCreate()

        val networkModule = NetworkModule()
        service = networkModule.movieService
        val urlAppender = ImageUrlAppender(service)
        movieDatabase = DatabaseModule().provideDatabase(context = applicationContext)



        repository =
            MovieRepositoryImpl(urlAppender, service, dispatcher, movieDatabase, applicationContext)
    }

    override fun getWorkManagerConfiguration(): Configuration {
       return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(RefreshMovieWorkerFactory(movieDatabase, service))
            .build()
    }
}