package com.example.photo_gallery.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photo_gallery.data.repository.FlickrRepository
import com.example.photo_gallery.viewmodel.GalleryViewModel

fun createGalleryViewModelFactory(
    repository: FlickrRepository,
    pagingManager: PagingManager
): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GalleryViewModel(repository, pagingManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}