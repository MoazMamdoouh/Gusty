package com.example.gusty.utilities

sealed class UiStateResult<out T> {
    object Loading : UiStateResult<Nothing>()
    data class Success<out T>(val response: T) : UiStateResult<T>()
    data class Failure(val exception: Throwable) : UiStateResult<Nothing>()
}