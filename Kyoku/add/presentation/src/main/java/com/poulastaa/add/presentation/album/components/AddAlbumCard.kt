package com.poulastaa.add.presentation.album.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.poulastaa.add.presentation.album.AddAlbumUiAction
import com.poulastaa.add.presentation.album.UiAlbum
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun AddAlbumCard(
    onAction: (AddAlbumUiAction.OnAlbumClick) -> Unit,
    album: UiAlbum,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable {
                onAction(
                    AddAlbumUiAction.OnAlbumClick(
                        album = album,
                        clickType = AddAlbumUiAction.ClickType.ADD
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.aspectRatio(1f),
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
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
                            color = MaterialTheme.colorScheme.primaryContainer
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
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
            )
        }

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.65f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = album.name,
                fontWeight = FontWeight.Medium
            )

            when {
                album.artist != null -> Text(
                    text = album.artist,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                )

                album.releaseYear != null -> Text(
                    text = "Year: ${album.releaseYear}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Checkbox(
            modifier = Modifier.animateContentSize(tween(500)),
            checked = album.isSelected,
            onCheckedChange = {
                onAction(
                    AddAlbumUiAction.OnAlbumClick(
                        album = album,
                        clickType = AddAlbumUiAction.ClickType.ADD
                    )
                )
            },
            colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.primary.copy(.7f),
                checkedColor = MaterialTheme.colorScheme.primary,
                checkmarkColor = MaterialTheme.colorScheme.background
            )
        )

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {
                onAction(
                    AddAlbumUiAction.OnAlbumClick(
                        album = album,
                        clickType = AddAlbumUiAction.ClickType.VIEW
                    )
                )
            }
        ) {
            Icon(
                imageVector = ArrowDownIcon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(.8f)
                    .rotate(-90f),
                tint = MaterialTheme.colorScheme.primary.copy(.8f)
            )
        }
    }
}