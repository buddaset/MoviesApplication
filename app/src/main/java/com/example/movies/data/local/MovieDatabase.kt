package com.example.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.data.local.dao.MovieDao
import com.example.movies.data.local.entity.MovieEntityDb

@Database(
    entities = [MovieEntityDb::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}