package com.poulastaa.main.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.SongIcon

@Composable
internal fun MainImageCard(
    url: String? = null,
    contentDescription: String? = null,
    errorIcon: ImageVector,
    modifier: Modifier,
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = CacheImageReq.imageReq(
            url = url,
            context = LocalContext.current
        ),
        contentScale = ContentScale.Crop,
        contentDescription = contentDescription,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(.4f),
                    strokeWidth = 1.5.dp,
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = errorIcon,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize(.65f)
                )
            }
        }
    )
}