package com.example.photo_gallery.utils

sealed class UiStateResult<out T> {
    object Loading : UiStateResult<Nothing>()
    data class Success<out T>(val data: T) : UiStateResult<T>()
    data class Error(val message: String) : UiStateResult<Nothing>()
}