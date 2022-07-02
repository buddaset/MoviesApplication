package com.example.movies.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationResponse(
    @SerialName("change_keys") val change_keys: List<String>,
    @SerialName("images") val images: ImageSettingResponse
)