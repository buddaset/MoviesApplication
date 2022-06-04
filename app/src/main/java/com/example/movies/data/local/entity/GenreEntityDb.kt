package com.example.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies.models.GenreData


@Entity(tableName = "genres")
data class GenreEntityDb(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
) {

   fun toGenreData() : GenreData =
       GenreData(id = id , name = name)
}
