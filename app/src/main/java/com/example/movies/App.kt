package com.example.movies

import android.app.Application
import com.example.movies.data.MovieRepository
import com.example.movies.data.MovieRepositoryImpl
import com.example.movies.data.dispatchers.IoDispatcher
import com.example.movies.data.utils.ImageUrlAppender
import com.example.movies.di.NetworkModule
import kotlinx.coroutines.Dispatchers

class App : Application() {

    lateinit var repository: MovieRepository
    private val dispatcher  = IoDispatcher(value = Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        val networkModule = NetworkModule()
        val service = networkModule.movieService
        val urlAppender = ImageUrlAppender(service)


        repository = MovieRepositoryImpl( urlAppender, service, dispatcher)
    }
}