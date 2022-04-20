package com.example.movies

import android.app.Application
import com.example.movies.data.MovieRepository
import com.example.movies.data.MovieRepositoryImpl
import com.example.movies.data.remote.ImageUrlAppender
import com.example.movies.di.NetworkModule

class App : Application() {

    lateinit var repository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        val networkModule = NetworkModule()
        val service = networkModule.movieService
        val urlAppender = ImageUrlAppender(service)


        repository = MovieRepositoryImpl( urlAppender, service)
    }
}