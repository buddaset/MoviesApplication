package com.example.movies.core.application

import android.app.Application
import androidx.work.Configuration
import com.example.movies.domain.repository.MoviesRepository
import com.example.movies.data.movies.repository.MoviesRepositoryImpl
import com.example.movies.core.dispatchers.IoDispatcher
import com.example.movies.data.core.local.MovieDatabase
import com.example.movies.data.core.remote.MovieApi
import com.example.movies.core.util.ImageUrlAppender
import com.example.movies.data.moviedetails.local.MovieDetailsLocalDataSourceImpl
import com.example.movies.data.moviedetails.remote.MovieDetailsRemoteDataSourceImpl
import com.example.movies.data.moviedetails.repository.MovieDetailsRepositoryImpl
import com.example.movies.data.movies.remote.MoviesRemoteDataSource
import com.example.movies.data.movies.remote.MoviesRemoteDataSourceImpl
import com.example.movies.data.workers.RefreshMovieWorkerFactory
import com.example.movies.di.DatabaseModule
import com.example.movies.di.NetworkModule
import com.example.movies.domain.repository.MovieDetailsRepository
import com.example.movies.domain.usecase.GetMovieDetailsUseCase
import com.example.movies.domain.usecase.GetMoviesBySearchUseCase
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import com.example.movies.presentation.moviedetails.viewmodel.DetailsMovieViewModel
import kotlinx.coroutines.Dispatchers

class App : Application(), Configuration.Provider {

    lateinit var useCase: UseCase
    private val dispatcher = IoDispatcher(value = Dispatchers.IO)
    private lateinit var movieDatabase: MovieDatabase
    private lateinit var service: MovieApi

    override fun onCreate() {
        super.onCreate()

        val networkModule = NetworkModule()
        service = networkModule.movieApi
        val urlAppender = ImageUrlAppender(service)
        movieDatabase = DatabaseModule().provideDatabase(context = applicationContext)

        val moviesRemoteDataSource: MoviesRemoteDataSource = MoviesRemoteDataSourceImpl(service)
        val movieDetailRemoteDataSource = MovieDetailsRemoteDataSourceImpl(service)
        val movieDetailLocalDataSource = MovieDetailsLocalDataSourceImpl(
            movieDatabase.actorDao(),
            movieDatabase.movieDetailDao()
        )


        val moviesRepository = MoviesRepositoryImpl(
            urlAppender,
            dispatcher,
            movieDatabase,
            applicationContext,
            moviesRemoteDataSource
        )
        val movieDetailRepository = MovieDetailsRepositoryImpl(
            movieDetailRemoteDataSource,
            movieDetailLocalDataSource,
            urlAppender
        )
        useCase = UseCase(moviesRepository, movieDetailRepository)

    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(RefreshMovieWorkerFactory(movieDatabase, service))
            .build()
    }
}


class UseCase(
    private val moviesRepository: MoviesRepository,
    private val movieDetailRepository: MovieDetailsRepository
) {


    fun getPopularMoviesUseCase() = GetPopularMoviesUseCase(moviesRepository)

    fun getMoviesBySearchUseCase() = GetMoviesBySearchUseCase(moviesRepository)

    fun getMovieDetailsUseCase() = GetMovieDetailsUseCase(movieDetailRepository)
}