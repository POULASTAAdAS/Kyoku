package com.poulastaa.core.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.dimens


@Composable
fun AppCacheImage(
    url: String?,
    modifier: Modifier = Modifier,
    loadingSize: Dp = 20.dp,
    shape: Shape = CircleShape,
    border: Dp = 1.4.dp,
    borderColor: Color = MaterialTheme.colorScheme.background,
    iconColor: Color = MaterialTheme.colorScheme.background,
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
                            color = iconColor
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
                            color = iconColor
                        )
                    )
                }
            }
        }
    )
}