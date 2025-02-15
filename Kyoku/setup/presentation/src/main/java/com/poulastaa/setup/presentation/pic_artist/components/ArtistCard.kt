package com.poulastaa.setup.presentation.pic_artist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.ui.components.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.setup.presentation.pic_artist.UiArtist

@Composable
fun ArtistCard(
    modifier: Modifier = Modifier,
    artist: UiArtist,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight(.78f)
                    .aspectRatio(1f),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = onClick
            ) {
                SubcomposeAsyncImage(
                    model = CacheImageReq.imageReq(
                        url = artist.cover,
                        context = LocalContext.current
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 1.5.dp,
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = UserIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(MaterialTheme.dimens.medium1),
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                )
            }

            this@Column.AnimatedVisibility(
                visible = artist.isSelected,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(MaterialTheme.dimens.small1)
            ) {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    tint = MaterialTheme.colorScheme.background,
                )
            }
        }

        Text(
            text = artist.name,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textDecoration = if (artist.isSelected) TextDecoration.Underline else null,
            fontWeight = if (artist.isSelected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.medium1),
            ) {
                ArtistCard(
                    modifier = Modifier.size(150.dp),
                    artist = UiArtist(
                        id = 1,
                        name = "Artist",
                        cover = "",
                        isSelected = true
                    ),
                    onClick = {}
                )
            }
        }
    }
}