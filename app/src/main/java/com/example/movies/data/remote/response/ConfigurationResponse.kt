package com.example.movies.data.remote.response

data class ConfigurationResponse(
    val change_keys: List<String>,
    val images: ImageSettingResponse
)