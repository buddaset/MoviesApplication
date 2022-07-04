package com.example.movies.core.navigation


interface Navigator {

    fun navigateTo(
        screen: Screen,
        addToBackStack: Boolean = true,
    )

    fun backTo(tag: String)

    fun back()
}
