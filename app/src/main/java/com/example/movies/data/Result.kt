package com.example.movies.data


sealed class Result<out S,  out E> {

    data class Success< S>(val data: S) : Result<S, Nothing>()

    data class Error< E>(val error: E) : Result<Nothing, E>()

}

inline fun <R> runOperationCatching(block : () -> R ) : Result<R, Throwable> {
    return try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Error(e)
    }
}


