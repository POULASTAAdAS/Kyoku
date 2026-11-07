package com.poulastaa.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.poulastaa.common.ui.design_system.IconAppLogo

/**
 * Loads [imageUrl] through the global Coil image loader. Shows the app logo while loading,
 * when the url is missing, or when loading fails.
 */
@Composable
fun NetworkImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderFraction: Float = 0.6f,
) {
    var isLoaded by remember(imageUrl) { mutableStateOf(false) }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (!isLoaded) Icon(
            imageVector = IconAppLogo,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(placeholderFraction),
        )

        if (!imageUrl.isNullOrBlank()) AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
            onSuccess = { isLoaded = true },
            onLoading = { isLoaded = false },
            onError = { isLoaded = false },
        )
    }
}
