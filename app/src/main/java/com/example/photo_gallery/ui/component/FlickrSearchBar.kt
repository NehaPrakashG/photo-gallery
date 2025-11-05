package com.example.photo_gallery.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.photo_gallery.R
import com.example.photo_gallery.ui.theme.Dimens

@Composable
fun FlickrSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    var localText by rememberSaveable { mutableStateOf(query) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SmallPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = localText,
            onValueChange = {
                localText = it
                onQueryChange(it)
            },
            placeholder = { Text(stringResource(R.string.search_hint)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
        )
    }
}