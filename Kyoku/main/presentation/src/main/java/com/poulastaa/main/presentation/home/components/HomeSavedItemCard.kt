package com.poulastaa.main.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.ui.SadIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.components.MainImageCard
import com.poulastaa.main.presentation.components.UiSaveItemType
import com.poulastaa.main.presentation.components.UiSavedItem

@Composable
internal fun HomeSavedItemCard(
    modifier: Modifier = Modifier,
    item: UiSavedItem,
    onClick: () -> Unit,
) {
    val shape = if (item.posters.size <= 3) CircleShape
    else MaterialTheme.shapes.extraSmall

    val errorIcon = when (item.type) {
        UiSaveItemType.PLAYLIST -> SongIcon
        UiSaveItemType.ALBUM -> FilterAlbumIcon
        UiSaveItemType.ARTIST -> FilterArtistIcon
        null -> SadIcon
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = shape,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .aspectRatio(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = shape
            ) {
                if (item.posters.size <= 3) MainImageCard(
                    url = item.posters.firstOrNull(),
                    errorIcon = errorIcon,
                    iconColor = MaterialTheme.colorScheme.background,
                    contentDescription = item.type?.name,
                    modifier = Modifier.fillMaxSize()
                )
                else Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.5f)
                    ) {
                        MainImageCard(
                            url = item.posters.getOrNull(0),
                            errorIcon = errorIcon,
                            iconColor = MaterialTheme.colorScheme.background,
                            contentDescription = item.type?.name,
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight()
                        )

                        MainImageCard(
                            url = item.posters.getOrNull(1),
                            errorIcon = errorIcon,
                            iconColor = MaterialTheme.colorScheme.background,
                            contentDescription = item.type?.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        MainImageCard(
                            url = item.posters.getOrNull(2),
                            errorIcon = errorIcon,
                            iconColor = MaterialTheme.colorScheme.background,
                            contentDescription = item.type?.name,
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight()
                        )

                        MainImageCard(
                            url = item.posters.getOrNull(3),
                            errorIcon = errorIcon,
                            iconColor = MaterialTheme.colorScheme.background,
                            contentDescription = item.type?.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

            Text(
                text = item.name,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = MaterialTheme.typography.titleMedium.fontSize,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            Column {
                Row(
                    modifier = Modifier
                        .height(90.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.medium1),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                    ) {
                        HomeSavedItemCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            item = UiSavedItem(
                                id = 1,
                                name = "That Cool Album",
                                type = UiSaveItemType.ALBUM
                            )
                        ) {}

                        HomeSavedItemCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            item = UiSavedItem(
                                id = 2,
                                name = "That Cool Artist",
                                type = UiSaveItemType.ARTIST
                            )
                        ) {}
                    }
                }

                Row(
                    modifier = Modifier
                        .height(90.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.medium1),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                    ) {
                        HomeSavedItemCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            item = UiSavedItem(
                                id = 1,
                                name = "Playlist 1",
                                posters = listOf("", "", ""),
                                type = UiSaveItemType.PLAYLIST
                            )
                        ) {}

                        HomeSavedItemCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            item = UiSavedItem(
                                id = 2,
                                name = "Playlist 2",
                                posters = listOf("", "", "", ""),
                                type = UiSaveItemType.PLAYLIST
                            )
                        ) {}
                    }
                }
            }
        }
    }
}