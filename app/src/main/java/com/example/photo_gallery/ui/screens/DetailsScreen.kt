package com.example.photo_gallery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.photo_gallery.R
import com.example.photo_gallery.data.model.FlickrPhoto
import com.example.photo_gallery.ui.component.PhotoItem
import com.example.photo_gallery.ui.theme.Dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    photo: FlickrPhoto,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = photo.title.takeIf { it.isNotBlank() } ?: "Photo Detail",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // <-- enables smooth scrolling
        ) {
            PhotoItem(
                photo = photo,
                sizeSuffix = "b", // “b” preserves aspect ratio perfectly
                showTitle = false
            )

            Spacer(modifier = Modifier.height(Dimens.MediumPadding))

            PhotoDetailsSection(photo)
        }
    }
}

@Composable
fun PhotoDetailsSection(
    photo: FlickrPhoto,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.ExtraPadding, vertical = Dimens.SemiPadding),
        horizontalAlignment = Alignment.Start,
    ) {
        photo.title.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(Dimens.SemiPadding))
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(Dimens.SemiPadding))

        photo.owner.takeIf { it.isNotBlank() }?.let {
            DetailRow(label = stringResource(R.string.details_owner), value = it)
            Spacer(modifier = Modifier.height(Dimens.SmallPadding))
        }

        val visibility = when (photo.isPublic) {
            1 -> "Public"
            0 -> "Private"
            else -> "Unknown"
        }
        DetailRow(label = stringResource(R.string.visibility), value = visibility)

        Spacer(modifier = Modifier.height(12.dp))

        photo.id.let {
            DetailRow(label =stringResource(R.string.photo_id), value = it)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(Dimens.SmallPadding))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End
        )
    }
}
