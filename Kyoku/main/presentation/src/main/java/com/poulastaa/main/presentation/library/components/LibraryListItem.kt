package com.poulastaa.main.presentation.library.components

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
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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

@Composable
internal fun LibraryListItem(
    modifier: Modifier = Modifier,
    title: String,
    posters: List<String>,
    shape: CornerBasedShape = MaterialTheme.shapes.small,
    type: UiSaveItemType?,
) {
    val errorIcon = when (type) {
        UiSaveItemType.PLAYLIST -> SongIcon
        UiSaveItemType.ALBUM -> FilterAlbumIcon
        UiSaveItemType.ARTIST -> FilterArtistIcon
        null -> SadIcon
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = shape,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .aspectRatio(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = shape,
            ) {
                if (posters.size <= 3) MainImageCard(
                    errorIcon = errorIcon,
                    url = posters.firstOrNull(),
                    modifier = Modifier.fillMaxSize(),
                    iconColor = MaterialTheme.colorScheme.background
                ) else Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.5f)
                    ) {
                        MainImageCard(
                            url = posters.getOrNull(0),
                            errorIcon = errorIcon,
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight(),
                            iconColor = MaterialTheme.colorScheme.background
                        )

                        MainImageCard(
                            url = posters.getOrNull(1),
                            errorIcon = errorIcon,
                            modifier = Modifier.fillMaxSize(),
                            iconColor = MaterialTheme.colorScheme.background
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        MainImageCard(
                            url = posters.getOrNull(2),
                            errorIcon = errorIcon,
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight(),
                            iconColor = MaterialTheme.colorScheme.background
                        )

                        MainImageCard(
                            url = posters.getOrNull(3),
                            errorIcon = errorIcon,
                            modifier = Modifier.fillMaxSize(),
                            iconColor = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                lineHeight = MaterialTheme.typography.titleMedium.fontSize,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            Column(
                modifier = Modifier.padding(MaterialTheme.dimens.medium1)
            ) {
                LibraryListItem(
                    modifier = Modifier.height(90.dp),
                    title = "That Cool Title",
                    posters = listOf(""),
                    type = UiSaveItemType.ARTIST
                )
            }
        }
    }
}