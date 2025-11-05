package com.example.photo_gallery.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.photo_gallery.data.model.GalleryState
import com.example.photo_gallery.data.repository.FlickrRepository
import com.example.photo_gallery.data.model.PhotoResult
import com.example.photo_gallery.utils.PagingManager
import com.example.photo_gallery.utils.UiStateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val repository: FlickrRepository,
    private val pagingManager: PagingManager,
    private val autoLoad: Boolean = true
) : BaseViewModel() {

    // Single Source of Truth
    private val _galleryState = MutableStateFlow(GalleryState())
    val galleryState: StateFlow<GalleryState> = _galleryState.asStateFlow()

    private val _query = MutableStateFlow("") // Tracks live user input
    private var fetchJob: Job? = null // CRITICAL: Ensure this is nulled after execution

    init {
        if (autoLoad) {
            fetchPhotos(query = "", reset = true, append = false)
        }
        observeSearch()
    }

    private fun observeSearch() {
        // Debounces user input and triggers search only after 400ms pause.
        _query.drop(1) // Skips the initial empty value
            .debounce(400)
            .distinctUntilChanged()
            .onEach { query ->
                fetchPhotos(query.trim(), reset = true, append = false)
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(query: String) {
        // Updates the flow watched by observeSearch
        _query.value = query.trim()
    }

    fun reload() = fetchPhotos(_galleryState.value.query, reset = true, append = false)

    fun loadNextPage() {
        val state = _galleryState.value
        // Guard against simultaneous requests or finished list
        if (state.isPaginating || state.isEndReached || state.isInitialLoading) return

        fetchPhotos(state.query, reset = false, append = true)
    }

    private fun fetchPhotos(query: String, reset: Boolean = false, append: Boolean = false) {

        // Use the query that triggered the fetch.
        val activeQuery = query.ifBlank { _galleryState.value.query }

        // 1. Guard against simultaneous requests and current job being active
        if (fetchJob?.isActive == true) return

        // 2. Determine Page and Reset Paging Manager
        if (reset) {
            pagingManager.reset()
        }

        val page = pagingManager.getNextPage()

        // Guard against duplicate pagination calls (e.g., if loadNextPage fires multiple times)
        if (append && page <= _galleryState.value.currentPage) return


        // 3. Update loading flags
        _galleryState.update {
            it.copy(
                isInitialLoading = reset,
                isPaginating = append,
                photos = if (reset) emptyList() else it.photos,
                error = null
            )
        }


        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = if (activeQuery.isBlank()) {
                    repository.getRecentPhotos(page)
                } else {
                    repository.searchPhotos(activeQuery, page)
                }
                updateState(result, activeQuery, page, append)
            } finally {
                fetchJob = null
            }
        }
    }

    private fun updateState(
        result: UiStateResult<PhotoResult>,
        query: String,
        page: Int,
        append: Boolean
    ) {
        when (result) {
            is UiStateResult.Success -> {
                pagingManager.updateState(result.data)

                _galleryState.update { old ->
                    val merged = if (append) old.photos + result.data.photos else result.data.photos

                    // Deduplicate photos
                    val uniquePhotos = merged.distinctBy { "${it.id}_${it.secret}" }

                    old.copy(
                        photos = uniquePhotos,
                        currentPage = result.data.page,
                        totalPages = result.data.pages,
                        totalPhotos = result.data.total,
                        query = query,
                        isInitialLoading = false,
                        isPaginating = false,
                        isEndReached = pagingManager.isEndReached(),
                        error = null
                    )
                }
            }

            is UiStateResult.Error -> {
                _galleryState.update {
                    it.copy(
                        isInitialLoading = false,
                        isPaginating = false,
                        error = result.message
                    )
                }
            }
            else -> Unit
        }
    }
}