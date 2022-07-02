package com.example.movies.data


sealed class Result<out S, out E> {

    data class Success<S>(val data: S) : Result<S, Nothing>()

    data class Error<E>(val error: E) : Result<Nothing, E>()

}

inline fun <R> runOperationCatching(block: () -> R): Result<R, Throwable> {
    return try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Error(e)
    }
}

inline fun <S, E, R> Result<S, E>.mapResult(block: (S) -> R): Result<R, E> =
    when (this) {
        is Result.Success -> Result.Success(data = block(this.data))
        is Result.Error -> Result.Error(error = this.error)
    }

inline fun <S, E> Result<S, E>.onSuccess(block: (S) -> Unit): Result<S, E> {
    if (this is Result.Success) {
        block(this.data)
    }
    return this

}

fun <S, E> Result<S, E>.getData(): S =
    when (this) {
        is Result.Success -> this.data
        is Result.Error -> throw IllegalStateException(error.toString())
    }

