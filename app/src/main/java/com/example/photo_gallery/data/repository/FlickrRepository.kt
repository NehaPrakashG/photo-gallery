package com.example.photo_gallery.data.repository

import com.example.photo_gallery.data.model.PhotoResult
import com.example.photo_gallery.utils.UiStateResult

interface FlickrRepository {
    suspend fun getRecentPhotos(page: Int): UiStateResult<PhotoResult>

    suspend fun searchPhotos(query: String, page: Int): UiStateResult<PhotoResult>
}