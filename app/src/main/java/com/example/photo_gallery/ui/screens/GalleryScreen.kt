package com.example.photo_gallery.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.photo_gallery.data.model.FlickrPhoto
import com.example.photo_gallery.ui.component.FlickrSearchBar
import com.example.photo_gallery.ui.component.LoadingView
import com.example.photo_gallery.ui.component.PhotoGrid
import com.example.photo_gallery.viewmodel.GalleryViewModel
import com.example.photo_gallery.R
import com.example.photo_gallery.ui.component.ErrorDialog

@Composable
fun GalleryScreen(
    onPhotoClick: (FlickrPhoto) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel
) {
    val galleryState by viewModel.galleryState.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()
    val snackbarHostState = remember { SnackbarHostState() }

    val shouldLoadMore by remember {
        derivedStateOf {
            val state = galleryState
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount

            if (totalItems == 0 || state.isInitialLoading || state.isPaginating) return@derivedStateOf false

            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val itemsRemaining = totalItems - lastVisibleIndex
             val threshold = 100

            val isNearEnd = itemsRemaining <= threshold

            isNearEnd && !state.isPaginating && !state.isEndReached
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNextPage()
        }
    }

    LaunchedEffect(galleryState.error) {
        if (galleryState.error != null && galleryState.photos.isNotEmpty()) {
            snackbarHostState.showSnackbar(message = galleryState.error!!)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            FlickrSearchBar(
                query = galleryState.query,
                onQueryChange = viewModel::onQueryChange,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val state = galleryState

            when {
                state.photos.isEmpty() && state.error != null -> {
                      ErrorDialog(
                          message = state.error.ifBlank { stringResource(R.string.error_generic) },
                          onDismiss = { viewModel.reload() }
                      )
                }

                state.isInitialLoading && state.photos.isEmpty() -> {
                    LoadingView(message = stringResource(R.string.loading_message))
                }

                state.photos.isNotEmpty() -> {
                    PhotoGrid(
                        listState = listState,
                        photos = state.photos,
                        onPhotoClick = onPhotoClick,
                        isLoadingMore = state.isPaginating
                    )
                }

                !state.isInitialLoading && state.photos.isEmpty() -> {
                    LoadingView(message = stringResource(R.string.empty_message))
                }
            }
        }
    }
}