package com.example.movies.data.local

import androidx.room.TypeConverter
import com.example.movies.models.GenreData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toSting(list: List<GenreData>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(value: String) : List<GenreData> {
        val mapType = object : TypeToken<List<GenreData>>() {}.type
        return Gson().fromJson(value, mapType)
    }


}