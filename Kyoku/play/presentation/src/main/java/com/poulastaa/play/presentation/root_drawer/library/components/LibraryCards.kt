package com.poulastaa.play.presentation.root_drawer.library.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.AddIcon
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.FavouriteIcon
import com.poulastaa.core.presentation.designsystem.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.FilterPlaylistIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SortTypeGridIcon
import com.poulastaa.core.presentation.designsystem.SortTypeListIcon
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.components.AppFilterChip
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReq
import com.poulastaa.core.presentation.ui.imageReqSongCover
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.play.presentation.root_drawer.library.LibraryUiEvent
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType

@Composable
fun LibraryPlaylistGird(
    modifier: Modifier = Modifier,
    urls: List<String>,
    name: String,
    header: String,
) {
    Column(
        modifier = modifier,
    ) {
        ImageGrid(
            modifier = Modifier
                .weight(.8f)
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally),
            header = header,
            urls = urls
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))

        Text(
            text = name,
            modifier = Modifier
                .weight(.15f)
                .align(Alignment.CenterHorizontally),
            maxLines = 1,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun LibraryPlaylistList(
    modifier: Modifier = Modifier,
    urls: List<String>,
    name: String,
    header: String,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            ImageGrid(
                modifier = Modifier.aspectRatio(1f),
                header = header,
                urls = urls,
                elevation = CardDefaults.cardElevation(0.dp)
            )

            Text(
                text = name,
                modifier = Modifier,
                maxLines = 2,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LibraryAlbumGrid(
    modifier: Modifier = Modifier,
    header: String,
    album: UiPrevAlbum,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(.78f),
            shape = MaterialTheme.shapes.extraSmall,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            SubcomposeAsyncImage(
                model = imageReq(
                    header = header,
                    url = album.coverImage
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = UserIcon,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                color = MaterialTheme.colorScheme.onBackground.copy(.7f)
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(MaterialTheme.dimens.small3)
                        )
                    }
                },
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 1.5.dp,
                            strokeCap = StrokeCap.Round,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        Text(
            text = album.name,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LibraryAlbumList(
    modifier: Modifier = Modifier,
    header: String,
    album: UiPrevAlbum,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {

            SubcomposeAsyncImage(
                model = imageReq(
                    header = header,
                    url = album.coverImage
                ),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small),
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_music),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                color = MaterialTheme.colorScheme.onBackground.copy(.7f)
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(MaterialTheme.dimens.small3)
                        )
                    }
                },
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 1.5.dp,
                            strokeCap = StrokeCap.Round,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )

            Text(
                text = album.name,
                modifier = Modifier,
                maxLines = 2,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun ImageGrid(
    modifier: Modifier = Modifier,
    header: String,
    shapes: CornerBasedShape = MaterialTheme.shapes.extraSmall,
    urls: List<String>,
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp
    ),
    color: CardColors = CardDefaults.cardColors(),
) {
    Card(
        modifier = modifier,
        shape = shapes,
        elevation = elevation,
        colors = color
    ) {
        if (urls.size < 4) {
            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = urls.getOrElse(0) { "" }
                ),
                modifier = Modifier
                    .aspectRatio(1f),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground.copy(.3f))
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
            ) {
                AsyncImage(
                    model = imageReqSongCover(
                        header = header,
                        url = urls.getOrElse(0) { "" }
                    ),
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(),
                    contentDescription = null
                )

                AsyncImage(
                    model = imageReqSongCover(
                        header = header,
                        url = urls.getOrElse(1) { "" }
                    ),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentDescription = null
                )
            }

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = imageReqSongCover(
                        header = header,
                        url = urls.getOrElse(2) { "" }
                    ),
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(),
                    contentDescription = null
                )

                AsyncImage(
                    model = imageReqSongCover(
                        header = header,
                        url = urls.getOrElse(3) { "" }
                    ),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryFilterRow(
    modifier: Modifier = Modifier,
    filterType: LibraryFilterType,
    viewType: LibraryViewType,
    onClick: (LibraryUiEvent) -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(.7f),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            FilterChip(
                selected = filterType == LibraryFilterType.ALL,
                onClick = { onClick(LibraryUiEvent.ToggleFilterType(LibraryFilterType.ALL)) },
                label = {
                    Text(
                        text = stringResource(id = R.string.all),
                        fontWeight = if (filterType == LibraryFilterType.ALL) FontWeight.SemiBold else FontWeight.Normal,
                        letterSpacing = 1.sp
                    )
                },
                shape = MaterialTheme.shapes.extraSmall,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(.8f),
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                )
            )

            AppFilterChip(
                text = stringResource(id = R.string.album),
                icon = FilterAlbumIcon,
                selected = filterType == LibraryFilterType.ALBUM,
                onClick = {
                    onClick(LibraryUiEvent.ToggleFilterType(LibraryFilterType.ALBUM))
                }
            )

            AppFilterChip(
                text = stringResource(id = R.string.artist),
                icon = FilterArtistIcon,
                selected = filterType == LibraryFilterType.ARTIST,
                onClick = {
                    onClick(LibraryUiEvent.ToggleFilterType(LibraryFilterType.ARTIST))
                }
            )

            AppFilterChip(
                text = stringResource(id = R.string.playlist),
                icon = FilterPlaylistIcon,
                selected = filterType == LibraryFilterType.PLAYLIST,
                onClick = {
                    onClick(LibraryUiEvent.ToggleFilterType(LibraryFilterType.PLAYLIST))
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    onClick(LibraryUiEvent.ToggleView)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary.copy(.8f)
                )
            ) {
                Icon(
                    imageVector = if (viewType == LibraryViewType.LIST) SortTypeGridIcon else SortTypeListIcon,
                    contentDescription = null,
                )
            }
        }
    }
}


@Composable
fun LibraryHeader(
    modifier: Modifier = Modifier,
    header: String,
    onAddClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = header,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground.copy(.8f),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onAddClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary.copy(.8f)
                )
            ) {
                Icon(
                    imageVector = AddIcon,
                    contentDescription = null
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(.5f))
        )
    }
}


@Composable
fun FavouriteCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            Column(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.primary.copy(.3f),
                            )
                        )
                    )
            ) {
                Icon(
                    imageVector = FavouriteIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .padding(MaterialTheme.dimens.small3),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary.copy(.8f)
                    else MaterialTheme.colorScheme.inversePrimary
                )
            }

            Text(
                text = stringResource(id = R.string.favourite),
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Row(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(MaterialTheme.dimens.large1)
        ) {
            ImageGrid(
                header = "",
                urls = listOf("")
            )
        }
    }
}