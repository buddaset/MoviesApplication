package com.example.movies.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreData(val id: Int, val name: String) : Parcelable