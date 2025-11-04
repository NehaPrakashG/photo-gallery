package com.example.photo_gallery.data.model

data class PhotoResult(
    val photos: List<FlickrPhoto>,
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val total: Int,
)


data class GalleryState(
    val photos: List<FlickrPhoto> = emptyList(),

    // Pagination & Metadata
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val totalPhotos: Int = 0,
    val query: String = "",

    // Loading State Flags
    val isInitialLoading: Boolean = false,
    val isPaginating: Boolean = false,
    val isEndReached: Boolean = false,

    // Error State
    val error: String? = null
)
