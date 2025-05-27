package com.example.winkcart_admin.data

sealed class ResponseStatus<out T> {
    data object Loading : ResponseStatus<Nothing>()
    data class Success<T>(val result: T) : ResponseStatus<T>()
    data class Error(val error: Throwable) : ResponseStatus<Nothing>()
}
