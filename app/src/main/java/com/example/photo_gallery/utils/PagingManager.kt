package com.example.photo_gallery.utils

import com.example.photo_gallery.data.model.PhotoResult


class PagingManager(private val startPage: Int = 1) {

    var currentPage: Int = startPage - 1

    var totalPages: Int = startPage


    fun getNextPage(): Int {
        currentPage += 1
        return currentPage
    }

    fun updateState(result: PhotoResult) {
        currentPage = result.page
        totalPages = result.pages
    }

    fun isEndReached(): Boolean {
        return currentPage >= totalPages
    }

    fun reset() {
        currentPage = startPage - 1
        totalPages = startPage
    }
}