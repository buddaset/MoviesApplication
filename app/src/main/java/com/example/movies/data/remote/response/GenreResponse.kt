package com.example.movies.data.remote.response

import com.example.movies.data.local.entity.GenreEntityDb

data class GenreResponse(
    val id: Int,
    val name: String
)

fun GenreResponse.toGenreEntityDb(): GenreEntityDb =
    GenreEntityDb(id = this.id , name = this.name)