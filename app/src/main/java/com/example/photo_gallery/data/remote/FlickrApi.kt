package com.example.photo_gallery.data.remote

import com.example.photo_gallery.data.model.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    companion object {
        const val DEFAULT_PER_PAGE = 50
    }

    @GET("services/rest/")
    suspend fun getRecentPhotos(
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE
    ): FlickrResponse

    @GET("services/rest/")
    suspend fun searchPhotos(
        @Query("method") method: String = "flickr.photos.search",
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE
    ): FlickrResponse
}