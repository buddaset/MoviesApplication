package com.example.movies.data.local

import androidx.room.TypeConverter
import com.example.movies.data.local.entity.GenreEntityDb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toSting(list: List<GenreEntityDb>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(value: String) : List<GenreEntityDb> {
        val mapType = object : TypeToken<List<GenreEntityDb>>() {}.type
        return Gson().fromJson(value, mapType)
    }


}