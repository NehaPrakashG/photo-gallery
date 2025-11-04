package com.example.photo_gallery.data.repository

import com.example.photo_gallery.data.model.FlickrResponse
import com.example.photo_gallery.data.model.PhotoResult
import com.example.photo_gallery.data.remote.FlickrApi
import com.example.photo_gallery.utils.UiStateResult
import com.example.photo_gallery.utils.handleNetworkErrors

class FlickrRepositoryImpl(private val api: FlickrApi) : FlickrRepository {

    override suspend fun getRecentPhotos(page: Int): UiStateResult<PhotoResult> {
        val rawResult = handleNetworkErrors { api.getRecentPhotos(page = page) }

        return when (rawResult) {
            is UiStateResult.Success -> mapFlickrResponse(rawResult.data)
            is UiStateResult.Error -> rawResult
            else -> UiStateResult.Error("Internal error: Unexpected state received.")
        }
    }

    override suspend fun searchPhotos(query: String, page: Int): UiStateResult<PhotoResult> {
        val rawResult = handleNetworkErrors { api.searchPhotos(query = query, page = page) }

        return when (rawResult) {
            is UiStateResult.Success -> mapFlickrResponse(rawResult.data)
            is UiStateResult.Error -> rawResult
            else -> UiStateResult.Error("Internal error: Unexpected state received.")
        }
    }

    private fun mapFlickrResponse(response: FlickrResponse): UiStateResult<PhotoResult> {
        if (response.stat != "ok" || response.photos == null) {
            return UiStateResult.Error("Flickr API status error.")
        }

        val flickrPhotos = response.photos
        val result = PhotoResult(
            photos = flickrPhotos.photo,
            page = flickrPhotos.page,
            pages = flickrPhotos.pages,
            perPage = flickrPhotos.perPage,
            total = flickrPhotos.total
        )
        return UiStateResult.Success(result)
    }
}