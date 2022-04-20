package com.example.movies.data.remote

import com.example.movies.data.remote.response.ImageSettingResponse

class ImageUrlAppender(private val service: MovieService) {

    lateinit var baseUrl: String
    lateinit var settingImage: ImageSettingResponse
    lateinit var posterSize: String
    lateinit var backDropSize: String
    lateinit var profileActorSize: String


    suspend fun getDetailImageUrl(pathImage: String): String {
        getConfiguration()
        return joinFullUrl(backDropSize, pathImage)
    }

    suspend fun getPosterImageUrl(pathImage: String): String {
        getConfiguration()
        return joinFullUrl(posterSize, pathImage)
    }

    suspend fun getActorImageUrl(pathImage: String): String {
        getConfiguration()
        return joinFullUrl(profileActorSize, pathImage)
    }


    private suspend fun getConfiguration() {
        if (baseUrl.isNotBlank()) return

        settingImage = service.loadConfiguration().images
        baseUrl = settingImage.secure_base_url
        posterSize = if (MEDIUM_SIZE in settingImage.poster_sizes) MEDIUM_SIZE else DEFAULT_SIZE
        backDropSize = if (LARGE_SIZE in settingImage.poster_sizes) MEDIUM_SIZE else DEFAULT_SIZE
        profileActorSize =
            if (MEDIUM_SIZE in settingImage.poster_sizes) MEDIUM_SIZE else DEFAULT_SIZE

    }

    private fun joinFullUrl(size: String, pathImage: String): String =
        baseUrl + size + pathImage


    companion object {

        private const val DEFAULT_SIZE = "origin"
        private const val SMALL_SIZE = "w185"
        private const val MEDIUM_SIZE = "w500"
        private const val LARGE_SIZE = "w780"
    }
}