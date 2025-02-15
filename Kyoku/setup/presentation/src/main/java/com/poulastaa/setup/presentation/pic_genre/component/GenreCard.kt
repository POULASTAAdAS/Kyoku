package com.poulastaa.setup.presentation.pic_genre.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.ui.components.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.setup.presentation.pic_genre.UiGenre

@Composable
internal fun GenreCard(
    modifier: Modifier = Modifier,
    genre: UiGenre,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .padding(end = MaterialTheme.dimens.small3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    model = CacheImageReq.imageReq(
                        url = genre.poster,
                        context = LocalContext.current
                    ),
                    contentDescription = genre.name,
                    modifier = Modifier.aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 1.5.dp,
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                    },
                    error = {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = FilterAlbumIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(MaterialTheme.dimens.small3),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Text(
                    text = genre.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            this@Card.AnimatedVisibility(
                visible = genre.isSelected,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small2)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(2.dp),
                    tint = MaterialTheme.colorScheme.background,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                GenreCard(
                    modifier = Modifier
                        .wrapContentWidth()
                        .width(140.dp)
                        .height(80.dp),
                    genre = UiGenre(
                        id = 1,
                        name = "Rock",
                        poster = "",
                        isSelected = isSystemInDarkTheme()
                    ),
                    onClick = {}
                )
            }
        }
    }
}