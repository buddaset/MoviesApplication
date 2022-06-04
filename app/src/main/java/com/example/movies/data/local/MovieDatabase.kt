package com.example.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.data.local.dao.*
import com.example.movies.data.local.entity.*

@Database(
    entities = [
        MovieEntityDb::class,
        MovieRemoteKeys::class,
        GenreEntityDb::class,
        MovieDetailsEntityDb::class,
        ActorEntityDb::class],

    version = 1,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao
    abstract fun genreDao(): GenreDao
    abstract fun movieDetailDao(): MovieDetailsDao
    abstract fun actorDao(): ActorDao
}