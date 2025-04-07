package com.poulastaa.view.presentation.saved.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon

@Composable
internal fun ViewSavedItemImage(
    modifier: Modifier = Modifier,
    poster: String?,
) {
    SubcomposeAsyncImage(
        model = CacheImageReq.imageReq(
            poster,
            LocalContext.current
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(.4f),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        error = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = FilterAlbumIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.7f),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}