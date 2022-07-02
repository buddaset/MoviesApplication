package com.example.movies.data.remote.response

import com.example.movies.data.remote.model.ActorDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorsResponse(
    @SerialName("cast") val actors: List<ActorDto>,

)
