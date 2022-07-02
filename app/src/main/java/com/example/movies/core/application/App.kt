package com.example.movies.core.application

import android.app.Application
import androidx.work.Configuration
import com.example.movies.domain.repository.MovieRepository
import com.example.movies.data.repository.MovieRepositoryImpl
import com.example.movies.core.dispatchers.IoDispatcher
import com.example.movies.data.local.MovieDatabase
import com.example.movies.data.remote.MovieApi
import com.example.movies.core.util.ImageUrlAppender
import com.example.movies.data.remote.MoviesRemoteDataSource
import com.example.movies.data.remote.MoviesRemoteDataSourceImpl
import com.example.movies.data.workers.RefreshMovieWorkerFactory
import com.example.movies.di.DatabaseModule
import com.example.movies.di.NetworkModule
import kotlinx.coroutines.Dispatchers

class App : Application(), Configuration.Provider {

    lateinit var repository: MovieRepository
    private val dispatcher = IoDispatcher(value = Dispatchers.IO)
    private lateinit var movieDatabase: MovieDatabase
    private lateinit var service: MovieApi

    override fun onCreate() {
        super.onCreate()

        val networkModule = NetworkModule()
        service = networkModule.movieApi
        val urlAppender = ImageUrlAppender(service)
        movieDatabase = DatabaseModule().provideDatabase(context = applicationContext)

        val moviesRemoteDataSource : MoviesRemoteDataSource = MoviesRemoteDataSourceImpl(service)



        repository =
            MovieRepositoryImpl(urlAppender, dispatcher, movieDatabase, applicationContext, moviesRemoteDataSource)
    }

    override fun getWorkManagerConfiguration(): Configuration {
       return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(RefreshMovieWorkerFactory(movieDatabase, service))
            .build()
    }
}