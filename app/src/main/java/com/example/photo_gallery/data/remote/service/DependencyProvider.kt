package com.example.photo_gallery.data.remote.service

import com.example.photo_gallery.BuildConfig
import com.example.photo_gallery.data.remote.FlickrApi
import com.example.photo_gallery.data.remote.NetworkClient
import com.example.photo_gallery.data.repository.FlickrRepository
import com.example.photo_gallery.data.repository.FlickrRepositoryImpl

object DependencyProvider {

    private const val BASE_URL = "https://www.flickr.com"
    private const val API_KEY = BuildConfig.FLICKR_API_KEY

    private val networkClient: NetworkClient by lazy {
        NetworkClient(
            baseUrl = BASE_URL,
            apiKey = API_KEY
        )
    }

    val flickrApi: FlickrApi
        get() = networkClient.flickrApi


    val flickrRepository: FlickrRepository by lazy {
        FlickrRepositoryImpl(api = flickrApi)
    }
}