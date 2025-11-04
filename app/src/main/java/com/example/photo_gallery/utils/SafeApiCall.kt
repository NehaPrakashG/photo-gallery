package com.example.photo_gallery.utils

import retrofit2.HttpException
import java.io.IOException

suspend fun <T : Any> handleNetworkErrors(
    apiCall: suspend () -> T
): UiStateResult<T> {
    return try {

        val response = apiCall()
        UiStateResult.Success(response)
    } catch (e: HttpException) {
        UiStateResult.Error("HTTP Error: ${e.code()}")
    } catch (e: IOException) {
        UiStateResult.Error("Network Error: Check your connection.")
    } catch (e: Exception) {
        UiStateResult.Error("An unknown error occurred: ${e.message}")
    }
}