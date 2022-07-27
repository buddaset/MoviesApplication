package com.example.movies.data.moviedetails.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDto(
    @SerialName("id") val id: Int,
    @SerialName("name")  val name: String,
    @SerialName("profile_path") var imageActorPath: String?
)