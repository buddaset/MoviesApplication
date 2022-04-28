package com.example.movies.data


sealed class Result<T> {

    class Loading<T> : Result<T>()

    class Success<T>(val data: T) : Result<T>()



    class Error<T>(val error: Throwable) : Result<T>()

}


