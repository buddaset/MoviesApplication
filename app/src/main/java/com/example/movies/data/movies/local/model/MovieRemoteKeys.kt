package com.example.movies.data.movies.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.awaitAll

@Entity(tableName = "movie_remote_keys")
data class MovieRemoteKeys(
    @PrimaryKey
    val id: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
