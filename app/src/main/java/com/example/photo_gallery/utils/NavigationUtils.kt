package com.example.photo_gallery.utils

import com.example.photo_gallery.data.model.FlickrPhoto
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object NavigationUtils {

    private val gson = Gson()

    /** Encode photo object to URL-safe JSON string. */
    fun encodePhoto(photo: FlickrPhoto): String {
        val json = gson.toJson(photo)
        return URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
    }

    /** Decode photo object back from encoded string. */
    fun decodePhoto(encoded: String): FlickrPhoto {
        val decodedJson = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
        return gson.fromJson(decodedJson, FlickrPhoto::class.java)
    }
}