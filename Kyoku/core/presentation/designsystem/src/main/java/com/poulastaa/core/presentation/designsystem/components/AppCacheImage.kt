package com.poulastaa.core.presentation.designsystem.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.disk.DiskCache
import coil.request.ImageRequest
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.SongIcon
import com.poulastaa.core.presentation.designsystem.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CacheImageReq @Inject constructor() {
    companion object REQUEST {
        @Volatile
        private var header: String = ""

        internal fun init(ds: DatastoreRepository) {
            CoroutineScope(Dispatchers.IO).launch {
                ds.readTokenOrCookie().collectLatest {
                    header = it
                }
            }
        }

        fun imageReq(
            url: String?,
            context: Context,
        ): ImageRequest {
            ImageLoader.Builder(context)
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.cacheDir.resolve("image_catch"))
                        .maxSizePercent(0.04)
                        .build()
                }
                .build()

            return ImageRequest.Builder(context)
                .addHeader(
                    name = if (!header.startsWith("Bearer")) "Cookie" else "Authorization",
                    value = header
                )
                .data(url)
                .build()
        }
    }
}

@Composable
fun AppCacheImage(
    url: String?,
    modifier: Modifier = Modifier,
    loadingSize: Dp = 20.dp,
    shape: Shape = CircleShape,
    border: Dp = 1.4.dp,
    borderColor: Color = MaterialTheme.colorScheme.background,
    errorIcon: ImageVector? = null,
    errorImage: Painter? = null,
) {
    SubcomposeAsyncImage(
        model = CacheImageReq.imageReq(
            url = url,
            context = LocalContext.current
        ),
        contentDescription = null,
        modifier = modifier,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = border,
                        color = borderColor,
                        shape = shape
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(loadingSize),
                    strokeWidth = 1.5.dp,
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = border,
                        color = borderColor,
                        shape = shape
                    ),
                contentAlignment = Alignment.Center
            ) {
                errorIcon?.let {
                    Image(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.small1),
                        alignment = Alignment.Center,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colorScheme.background
                        )
                    )
                }

                errorImage?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.small1),
                        alignment = Alignment.Center,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colorScheme.background
                        )
                    )
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.primary
        ) {
            AppCacheImage(
                url = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(120.dp)
                    .border(
                        width = 1.2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(.3f),
                        shape = CircleShape
                    ),
                errorIcon = SongIcon
            )
        }
    }
}