package com.example.movies.data.core.local

import androidx.room.TypeConverter
import com.example.movies.data.movies.local.model.GenreEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toSting(list: List<GenreEntity>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(value: String) : List<GenreEntity> {
        val mapType = object : TypeToken<List<GenreEntity>>() {}.type
        return Gson().fromJson(value, mapType)
    }


}