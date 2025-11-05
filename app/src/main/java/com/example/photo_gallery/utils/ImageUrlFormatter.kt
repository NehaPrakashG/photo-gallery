package com.example.photo_gallery.utils

object ImageUrlFormatter {
    fun buildImageUrl(
        server: String?,
        id: String?,
        secret: String?,
        sizeSuffix: String? = null // "q" for grid, "b" for detail
    ): String? {
        if (server.isNullOrBlank() || id.isNullOrBlank() || secret.isNullOrBlank()) return null
        val suffix = sizeSuffix?.let { "_$it" } ?: ""
        return "https://live.staticflickr.com/$server/${id}_${secret}$suffix.jpg"
    }
}