package com.example.movies.data.movies.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies.domain.model.Genre


@Entity(tableName = "genres")
data class GenreEntityDb(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
) {

   fun toDomain() : Genre =
       Genre(id = id , name = name)
}
