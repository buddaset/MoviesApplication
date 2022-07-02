package com.example.movies.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageSettingResponse(
    @SerialName("backdrop_sizes")  val backdrop_sizes: List<String>,
    @SerialName("base_url")  val base_url: String,
    @SerialName("logo_sizes")  val logo_sizes: List<String>,
    @SerialName("poster_sizes")  val poster_sizes: List<String>,
    @SerialName("profile_sizes")  val profile_sizes: List<String>,
    @SerialName("secure_base_url") val secure_base_url: String,
    @SerialName("still_sizes")  val still_sizes: List<String>
)