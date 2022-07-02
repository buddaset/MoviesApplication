package com.example.movies.core.util

import com.example.movies.data.remote.MovieApi
import com.example.movies.data.remote.response.ImageSettingResponse

class ImageUrlAppender(private val service: MovieApi) {

    private var _baseUrl: String? = null
    private lateinit var settingImage: ImageSettingResponse
    private lateinit var posterSize: String
   private lateinit var backDropSize: String
   private lateinit var profileActorSize: String

   //todo change get baseImage and correct size for picture
   val  baseImageUrl = "https://image.tmdb.org/t/p/w300"




    suspend fun getDetailImageUrl(pathImage: String?): String? {
        getConfiguration()
        return joinFullUrl(backDropSize, pathImage)
    }



    suspend fun getPosterImageUrl(pathImage: String?): String? {
        getConfiguration()
        return joinFullUrl(posterSize, pathImage)
    }

    suspend fun getActorImageUrl(pathImage: String?): String? {
        getConfiguration()

        return joinFullUrl(profileActorSize, pathImage)
    }


    private suspend fun getConfiguration() {
        if (_baseUrl != null) return

        settingImage = service.loadConfiguration().images
        _baseUrl = settingImage.secure_base_url
        posterSize = if (MEDIUM_SIZE in settingImage.poster_sizes) MEDIUM_SIZE else DEFAULT_SIZE
        backDropSize = if (MEDIUM_SIZE in settingImage.poster_sizes) MEDIUM_SIZE else DEFAULT_SIZE
        profileActorSize = if (SMALL_SIZE in settingImage.poster_sizes) SMALL_SIZE else DEFAULT_SIZE

    }

    private fun joinFullUrl(size: String, pathImage: String?): String? =
        if (pathImage.isNullOrEmpty()) null else _baseUrl + size + pathImage


    companion object {



        private const val DEFAULT_SIZE = "origin"
        private const val SMALL_SIZE = "w185"
        private const val MEDIUM_SIZE = "w500"
        private const val LARGE_SIZE = "w780"
    }
}