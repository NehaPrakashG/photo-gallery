package com.example.photo_gallery.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.photo_gallery.R
import com.example.photo_gallery.data.model.FlickrPhoto
import com.example.photo_gallery.ui.theme.Dimens
import com.example.photo_gallery.utils.ImageUrlFormatter

@Composable
fun PhotoItem(
    photo: FlickrPhoto,
    sizeSuffix: String? = null,
    showTitle: Boolean = false,
    itemSize: Dp? = null,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val imageUrl = ImageUrlFormatter.buildImageUrl(
        server = photo.server,
        id = photo.id,
        secret = photo.secret,
        sizeSuffix = sizeSuffix
    )
    val photoTitle = photo.title ?: "Photo"

    Card(
        modifier = Modifier
            .then(
                if (itemSize != null) Modifier.size(itemSize)
                else Modifier.fillMaxWidth()
            )
            .heightIn(min = Dimens.CardHeight)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.ic_launcher_background)
                    .build(),
                contentDescription = photoTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            if (showTitle) {
                PhotoTitleOverlay(
                    title = photoTitle,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}