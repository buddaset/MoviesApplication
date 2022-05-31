package com.example.movies.di

import android.content.Context
import androidx.room.Room
import com.example.movies.data.local.MovieDatabase
import com.example.movies.models.MovieData

class DatabaseModule {
        private var INSTANCE : MovieDatabase? = null
    fun provideDatabase(context: Context) : MovieDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance =  Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db")
                .build()
            INSTANCE = instance
            instance
        }

    }
}