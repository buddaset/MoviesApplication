package com.example.movies.di

import android.content.Context
import androidx.room.Room
import com.example.movies.data.local.MovieDatabase

class DatabaseModule {
        private var INSTANCE : MovieDatabase? = null
    fun provideDatabase(context: Context) : MovieDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance =  Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db")
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }

    }
}