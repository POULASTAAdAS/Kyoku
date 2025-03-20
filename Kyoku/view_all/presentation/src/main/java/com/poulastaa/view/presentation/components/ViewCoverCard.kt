package com.poulastaa.view.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.poulastaa.core.presentation.designsystem.ui.UserIcon

@Composable
internal fun ViewCoverCard(
    isNotAlbum: Boolean,
    isTypeArtist: Boolean,
    covers: List<String>?,
    cover: String,
) {
    if (isNotAlbum && isTypeArtist.not() && covers != null && covers.size >= 4) Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
        ) {
            SubcomposeAsyncImage(
                model = CacheImageReq.imageReq(
                    covers.first(),
                    LocalContext.current
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillBounds,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(.15f),
                            strokeWidth = 3.dp,
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
                            imageVector = UserIcon,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(.7f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )

            SubcomposeAsyncImage(
                model = CacheImageReq.imageReq(
                    covers[1],
                    LocalContext.current
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(.15f),
                            strokeWidth = 3.dp,
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
                            imageVector = UserIcon,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(.7f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                model = CacheImageReq.imageReq(
                    covers[2],
                    LocalContext.current
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillBounds,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(.15f),
                            strokeWidth = 3.dp,
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
                            imageVector = UserIcon,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(.7f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )

            SubcomposeAsyncImage(
                model = CacheImageReq.imageReq(
                    covers[3],
                    LocalContext.current
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(.15f),
                            strokeWidth = 3.dp,
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
                            imageVector = UserIcon,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(.7f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    } else SubcomposeAsyncImage(
        model = CacheImageReq.imageReq(
            url = covers?.randomOrNull() ?: cover,
            LocalContext.current
        ),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(.15f),
                    strokeWidth = 3.dp,
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
                    imageVector = if (isTypeArtist) UserIcon else FilterAlbumIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.7f),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}