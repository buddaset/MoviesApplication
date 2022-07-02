package com.example.movies.data.remote.model

import com.example.movies.data.local.entity.GenreEntityDb
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

fun GenreDto.toGenreEntityDb(): GenreEntityDb =
    GenreEntityDb(id = this.id , name = this.name)