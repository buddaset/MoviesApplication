package com.example.movies.data.movies.remote.model

import com.example.movies.data.movies.local.model.GenreEntityDb
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

fun GenreDto.toEntity(): GenreEntityDb =
    GenreEntityDb(id = this.id , name = this.name)