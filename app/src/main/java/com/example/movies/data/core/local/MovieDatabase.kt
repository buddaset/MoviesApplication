package com.example.movies.data.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.data.favoritemovies.local.dao.FavoriteDao
import com.example.movies.data.favoritemovies.local.model.FavoriteIdEntity
import com.example.movies.data.moviedetails.local.dao.ActorDao
import com.example.movies.data.moviedetails.local.dao.MovieDetailsDao
import com.example.movies.data.moviedetails.local.model.*
import com.example.movies.data.movies.local.dao.GenreDao
import com.example.movies.data.movies.local.dao.MovieDao
import com.example.movies.data.movies.local.dao.MovieRemoteKeysDao
import com.example.movies.data.movies.local.model.*


@Database(
    entities = [
        MovieEntity::class,
        MovieRemoteKeys::class,
        GenreEntity::class,
        MovieDetailsEntity::class,
        ActorEntity::class,
        FavoriteIdEntity::class],

    version = 1,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao
    abstract fun genreDao(): GenreDao
    abstract fun movieDetailDao(): MovieDetailsDao
    abstract fun actorDao(): ActorDao
    abstract fun favoriteDao(): FavoriteDao

}

//    companion object {
//        private val CALLBACK = object : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                db.execSQL(
//                    """
//                        CREATE TRIGGER update_movie AFTER INSERT ON movies
//                        BEGIN
//                        UPDATE movies
//                        END;
//                    """
//                )
//    }
//}

