package com.poulastaa.explore.presentation.search.album.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.ExploreAlbumUiAction
import com.poulastaa.explore.presentation.search.album.SEARCH_ALBUM_FILTER_TYPE

@Composable
internal fun ExploreAlbumCard(
    album: ExploreUiItem,
    filterType: SEARCH_ALBUM_FILTER_TYPE,
    onAction: (ExploreAlbumUiAction) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable {
                onAction(ExploreAlbumUiAction.OnAlbumClick(album.id))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.aspectRatio(1f),
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            SubcomposeAsyncImage(
                model = CacheImageReq.imageReq(
                    album.poster,
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

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(
                    modifier = Modifier.fillMaxWidth(
                        if (filterType == SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR &&
                            album.artist != null
                        ) .7f else 1f
                    ),
                    text = album.title,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))

                if (filterType == SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR &&
                    album.artist != null
                ) Text(
                    text = "Year: ${album.releaseYear}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.small1))

            Text(
                text = album.artist ?: "Year: ${album.releaseYear}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {
                onAction(ExploreAlbumUiAction.OnAlbumClick(album.id))
            }
        ) {
            Icon(
                imageVector = ArrowBackIcon,
                contentDescription = null,
                modifier = Modifier.rotate(180f)
            )
        }
    }
}