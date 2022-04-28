package com.example.movies.data.remote.response

import com.google.gson.annotations.SerializedName

data class ActorResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name")  val name: String,
    @SerializedName("profile_path")   val imageActorPath: String?
)