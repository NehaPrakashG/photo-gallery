package com.example.photo_gallery.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.photo_gallery.data.model.FlickrPhoto
import com.example.photo_gallery.ui.theme.Dimens

@Composable
fun PhotoGrid(
    listState: LazyGridState,
    photos: List<FlickrPhoto>,
    onPhotoClick: (FlickrPhoto) -> Unit,
    isLoadingMore: Boolean,
) {

    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.GridSpacing),
        verticalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
        horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing)
    ) {
        items(
            items = photos,
            key = { photo -> "${photo.id}_${photo.secret}" }
        ) { photo ->
            PhotoItem(
                photo = photo,
                sizeSuffix = "q",  // smaller images = faster load
                showTitle = true,
                onClick = { onPhotoClick(photo) }
            )
        }

        if (isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingFooter()
            }
        }
    }
}