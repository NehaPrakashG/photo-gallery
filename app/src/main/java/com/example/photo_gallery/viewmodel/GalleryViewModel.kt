package com.example.photo_gallery.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.photo_gallery.data.model.GalleryState
import com.example.photo_gallery.data.repository.FlickrRepository
import com.example.photo_gallery.utils.PagingManager
import com.example.photo_gallery.utils.UiStateResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GalleryViewModel (
    private val repository: FlickrRepository,
    private val pagingManager: PagingManager
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(GalleryState(isInitialLoading = true))
    val uiState: StateFlow<GalleryState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        loadPhotos(isInitial = true)
    }

    fun loadPhotos(isInitial: Boolean = false) {


        if (fetchJob?.isActive == true) return
        if (!isInitial && (uiState.value.isPaginating || uiState.value.isEndReached)) return
        if (isInitial && uiState.value.isInitialLoading && uiState.value.photos.isNotEmpty()) return

        fetchJob = viewModelScope.launch {

            // Handle reset for initial/refresh loads
            if (isInitial) {
                pagingManager.reset()
                _uiState.update { it.copy(photos = emptyList()) }
            }

            _uiState.update {
                it.copy(
                    isInitialLoading = isInitial,
                    isPaginating = !isInitial,
                    error = null
                )
            }

            val nextPage = pagingManager.getNextPage()

            when (val result = repository.getRecentPhotos(page = nextPage)) {
                is UiStateResult.Success -> {
                    pagingManager.updateState(result.data)

                    _uiState.update { currentState ->
                        currentState.copy(
                            photos = currentState.photos + result.data.photos,
                            currentPage = result.data.page,
                            totalPages = result.data.pages,
                            totalPhotos = result.data.total,
                            isInitialLoading = false,
                            isPaginating = false,
                            isEndReached = pagingManager.isEndReached()
                        )
                    }
                }
                is UiStateResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isInitialLoading = false,
                            isPaginating = false,
                            error = result.message
                        )
                    }
                    reportGlobalError(result.message)
                }

                is UiStateResult.Loading -> {}
            }
        }
    }


    fun loadNextPage() {
        if (!uiState.value.isPaginating && !uiState.value.isEndReached) {
            loadPhotos()
        }
    }
}