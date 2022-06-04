package com.example.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.data.local.dao.GenreDao
import com.example.movies.data.local.dao.MovieDao
import com.example.movies.data.local.dao.MovieDetailsDao
import com.example.movies.data.local.dao.MovieRemoteKeysDao
import com.example.movies.data.local.entity.GenreEntityDb
import com.example.movies.data.local.entity.MovieDetailsEntityDb
import com.example.movies.data.local.entity.MovieEntityDb
import com.example.movies.data.local.entity.MovieRemoteKeys

@Database(
    entities = [
        MovieEntityDb::class,
        MovieRemoteKeys::class,
        GenreEntityDb::class,
        MovieDetailsEntityDb::class],

    version = 1,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao
    abstract fun genreDao(): GenreDao
    abstract fun movieDetailDao(): MovieDetailsDao
}