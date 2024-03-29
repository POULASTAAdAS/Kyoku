package com.poulastaa.kyoku.presentation.screen.home_root.library.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.common.UiAlbum
import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev
import com.poulastaa.kyoku.data.model.screens.library.Artist
import com.poulastaa.kyoku.data.model.screens.library.FilterChip
import com.poulastaa.kyoku.data.model.screens.library.LibraryUiEvent
import com.poulastaa.kyoku.data.model.screens.library.PinnedData
import com.poulastaa.kyoku.data.model.screens.library.PinnedDataType
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.background
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun LazyGridScope.filterChips(
    span: GridItemSpan,
    isGrid: Boolean,
    filterChip: FilterChip,
    filterByPlaylist: (LibraryUiEvent.FilterChipClick.PlaylistType) -> Unit,
    filterByAlbum: (LibraryUiEvent.FilterChipClick.AlbumType) -> Unit,
    filterByArtist: (LibraryUiEvent.FilterChipClick.ArtistType) -> Unit,
    sortOrderClick: (LibraryUiEvent.ItemClick.SortTypeClick) -> Unit
) {
    item(
        span = { span }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                CustomFilterChip(
                    isSelected = filterChip.isPlaylist,
                    icon = R.drawable.ic_filter_playlist,
                    type = PinnedDataType.PLAYLIST.title
                ) {
                    filterByPlaylist.invoke(LibraryUiEvent.FilterChipClick.PlaylistType)
                }

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                CustomFilterChip(
                    isSelected = filterChip.isAlbum,
                    icon = R.drawable.ic_filter_album,
                    type = PinnedDataType.ALBUM.title
                ) {
                    filterByAlbum.invoke(LibraryUiEvent.FilterChipClick.AlbumType)
                }

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                CustomFilterChip(
                    isSelected = filterChip.isArtist,
                    icon = R.drawable.ic_filter_artist,
                    type = PinnedDataType.ARTIST.title
                ) {
                    filterByArtist.invoke(LibraryUiEvent.FilterChipClick.ArtistType)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = !filterChip.isAlbum &&
                            !filterChip.isArtist &&
                            !filterChip.isPlaylist
                ) {
                    val temp = remember {
                        !filterChip.isAlbum &&
                                !filterChip.isArtist &&
                                !filterChip.isPlaylist
                    }

                    if (temp)
                        IconButton(
                            onClick = {
                                sortOrderClick.invoke(LibraryUiEvent.ItemClick.SortTypeClick)
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (isGrid) R.drawable.list
                                    else R.drawable.grid
                                ),
                                contentDescription = null
                            )
                        }
                }
            }
        }
    }
}


fun LazyGridScope.playlist(
    playlistPrev: List<UiPlaylistPrev>,
    isGrid: Boolean,
    sizeIfGrid: Dp,
    sizeIfList: Dp,
    isCookie: Boolean,
    headerValue: String,
    scope: CoroutineScope,
    onCreatePlaylistClick: (LibraryUiEvent.ItemClick.CreatePlaylistClick) -> Unit,
    onLongClick: (LibraryUiEvent.ItemClick.PlaylistLongClick) -> Unit,
    onClick: (LibraryUiEvent.ItemClick.PlaylistClick) -> Unit
) {
    libraryScreenItemHeading(
        heading = "Playlist",
        isGrid = isGrid,
        onClick = {
            scope.launch {
                onCreatePlaylistClick.invoke(LibraryUiEvent.ItemClick.CreatePlaylistClick)
            }
        }
    )

    headLineSeparator(isGrid)

    libraryScreenItemPlaylist(
        playlistPrev = playlistPrev,
        sizeIfGrid = sizeIfGrid, // if (isSmallPhone) 120.dp else 130.dp,
        sizeIfList = sizeIfList, //80.dp,
        isCookie = isCookie,
        headerValue = headerValue,
        isGrid = isGrid,
        scope = scope,
        onLongClick = onLongClick,
        onClick = onClick
    )
}


fun LazyGridScope.album(
    albums: List<UiAlbum>,
    isGrid: Boolean,
    sizeIfList: Dp,
    sizeIfGrid: Dp,
    isCookie: Boolean,
    headerValue: String,
    scope: CoroutineScope,
    onCreateAlbumClick: (LibraryUiEvent.ItemClick.AddAlbumClick) -> Unit,
    onLongClick: (LibraryUiEvent.ItemClick.AlbumLongClick) -> Unit,
    onClick: (LibraryUiEvent.ItemClick.AlbumClick) -> Unit
) {
    libraryScreenItemHeading(
        heading = "Album",
        isGrid = isGrid,
        onClick = {
            scope.launch {
                onCreateAlbumClick.invoke(LibraryUiEvent.ItemClick.AddAlbumClick)
            }
        }
    )

    headLineSeparator(isGrid)


    libraryScreenItemAlbum(
        albums = albums,
        sizeIfGrid = sizeIfGrid,
        sizeIfList = sizeIfList,
        isCookie = isCookie,
        headerValue = headerValue,
        isGrid = isGrid,
        scope = scope,
        onLongClick = onLongClick,
        onClick = onClick
    )
}


fun LazyGridScope.artist(
    artists: List<Artist>,
    isGrid: Boolean,
    size: Dp,
    isCookie: Boolean,
    headerValue: String,
    scope: CoroutineScope,
    onAddArtistClick: (LibraryUiEvent.ItemClick.AddArtistClick) -> Unit,
    onLongClick: (LibraryUiEvent.ItemClick.ArtistLongClick) -> Unit,
    onClick: (LibraryUiEvent.ItemClick.ArtistClick) -> Unit
) {
    libraryScreenItemHeading(
        heading = "Artist",
        isGrid = isGrid,
        onClick = {
            scope.launch {
                onAddArtistClick.invoke(LibraryUiEvent.ItemClick.AddArtistClick)
            }
        }
    )

    headLineSeparator(isGrid)

    libraryScreenItemArtist(
        artistPrev = artists,
        sizeIfGrid = size,
        sizeIfList = size,
        isCookie = isCookie,
        headerValue = headerValue,
        isGrid = isGrid,
        scope = scope,
        onLongClick = onLongClick,
        onClick = onClick
    )
}


fun LazyGridScope.favourite(
    span: GridItemSpan,
    scope: CoroutineScope,
    onLongClick: (LibraryUiEvent.ItemClick.FavouriteLongClick) -> Unit,
    onClick: (LibraryUiEvent.ItemClick.FavouriteClick) -> Unit
) {
    item(
        span = {
            span
        }
    ) {
        FavouritePrev(
            modifier = Modifier
                .fillMaxWidth(.2f)
                .height(80.dp),
            onLongClick = {
                scope.launch {
                    onLongClick.invoke(LibraryUiEvent.ItemClick.FavouriteLongClick)
                }
            },
            onClick = {
                scope.launch {
                    onClick.invoke(LibraryUiEvent.ItemClick.FavouriteClick)
                }
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun LazyGridScope.libraryScreenItemPlaylist(
    playlistPrev: List<UiPlaylistPrev>,
    sizeIfGrid: Dp,
    sizeIfList: Dp,
    isCookie: Boolean,
    headerValue: String,
    isGrid: Boolean,
    scope: CoroutineScope,
    onLongClick: (LibraryUiEvent.ItemClick.PlaylistLongClick) -> Unit,
    onClick: (LibraryUiEvent.ItemClick.PlaylistClick) -> Unit
) {
    items(playlistPrev.size) {
        if (isGrid)
            LibraryScreenPlaylistGridView(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small1)
                    .size(sizeIfGrid)
                    .combinedClickable(
                        onLongClick = {
                            scope.launch {
                                onLongClick.invoke(
                                    LibraryUiEvent.ItemClick.PlaylistLongClick(
                                        playlistPrev[it].name
                                    )
                                )
                            }
                        },
                        onClick = {
                            scope.launch {
                                onClick.invoke(
                                    LibraryUiEvent.ItemClick.PlaylistClick(
                                        id = playlistPrev[it].id,
                                        name = playlistPrev[it].name
                                    )
                                )
                            }
                        }
                    ),
                isCookie = isCookie,
                authHeader = headerValue,
                name = playlistPrev[it].name,
                imageUrls = playlistPrev[it].listOfUrl
            )
        else
            LibraryScreenPlaylistListView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.small1)
                    .height(sizeIfList)
                    .combinedClickable(
                        onLongClick = {
                            scope.launch {
                                onLongClick.invoke(
                                    LibraryUiEvent.ItemClick.PlaylistLongClick(
                                        playlistPrev[it].name
                                    )
                                )
                            }
                        },
                        onClick = {
                            scope.launch {
                                onClick.invoke(
                                    LibraryUiEvent.ItemClick.PlaylistClick(
                                        playlistPrev[it].id,
                                        name = playlistPrev[it].name
                                    )
                                )
                            }
                        }
                    ),
                isCookie = isCookie,
                authHeader = headerValue,
                name = playlistPrev[it].name,
                imageUrls = playlistPrev[it].listOfUrl
            )
    }
}


fun LazyGridScope.libraryScreenItemAlbum(
    albums: List<UiAlbum>,
    sizeIfGrid: Dp,
    sizeIfList: Dp,
    isCookie: Boolean,
    headerValue: String,
    isGrid: Boolean,
    scope: CoroutineScope,
    onLongClick: (LibraryUiEvent.ItemClick.AlbumLongClick) -> Unit,
    onClick: (LibraryUiEvent.ItemClick.AlbumClick) -> Unit
) {
    items(albums.size) {
        if (isGrid)
            LibraryScreenAlbumAndArtistGridView(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small3)
                    .size(sizeIfGrid),
                isAlbum = true,
                name = albums[it].name,
                imageUrl = albums[it].coverImage,
                isCookie = isCookie,
                headerValue = headerValue,
                onLongClick = {
                    scope.launch {
                        onLongClick.invoke(
                            LibraryUiEvent.ItemClick.AlbumLongClick(
                                name = albums[it].name
                            )
                        )
                    }
                },
                onClick = {
                    scope.launch {
                        onClick.invoke(
                            LibraryUiEvent.ItemClick.AlbumClick(
                                name = albums[it].name
                            )
                        )
                    }
                }
            )
        else
            LibraryScreenAlbumAndArtistListView(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small3)
                    .size(sizeIfList),
                isAlbum = true,
                name = albums[it].name,
                imageUrl = albums[it].coverImage,
                isCookie = isCookie,
                headerValue = headerValue,
                onLongClick = {
                    scope.launch {
                        onLongClick.invoke(
                            LibraryUiEvent.ItemClick.AlbumLongClick(
                                name = albums[it].name
                            )
                        )
                    }
                },
                onClick = {
                    scope.launch {
                        onClick.invoke(
                            LibraryUiEvent.ItemClick.AlbumClick(
                                name = albums[it].name
                            )
                        )
                    }
                }
            )
    }
}


fun LazyGridScope.libraryScreenItemArtist(
    artistPrev: List<Artist>,
    sizeIfGrid: Dp,
    sizeIfList: Dp,
    isCookie: Boolean,
    headerValue: String,
    isGrid: Boolean,
    scope: CoroutineScope,
    onLongClick: (LibraryUiEvent.ItemClick.ArtistLongClick) -> Unit,
    onClick: (LibraryUiEvent.ItemClick.ArtistClick) -> Unit
) {
    items(artistPrev.size) {
        if (isGrid)
            LibraryScreenAlbumAndArtistGridView(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small3)
                    .size(sizeIfGrid),
                isAlbum = false,
                name = artistPrev[it].name,
                imageUrl = artistPrev[it].coverImage,
                isCookie = isCookie,
                headerValue = headerValue,
                onLongClick = {
                    scope.launch {
                        onLongClick.invoke(
                            LibraryUiEvent.ItemClick.ArtistLongClick(
                                id = artistPrev[it].id,
                                name = artistPrev[it].name
                            )
                        )
                    }
                },
                onClick = {
                    scope.launch {
                        onClick.invoke(
                            LibraryUiEvent.ItemClick.ArtistClick(
                                id = artistPrev[it].id,
                                name = artistPrev[it].name
                            )
                        )
                    }
                }
            )
        else
            LibraryScreenAlbumAndArtistListView(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small3)
                    .size(sizeIfList),
                isAlbum = false,
                name = artistPrev[it].name,
                imageUrl = artistPrev[it].coverImage,
                isCookie = isCookie,
                headerValue = headerValue,
                onLongClick = {
                    scope.launch {
                        onLongClick.invoke(
                            LibraryUiEvent.ItemClick.ArtistLongClick(
                                id = artistPrev[it].id,
                                name = artistPrev[it].name
                            )
                        )
                    }
                },
                onClick = {
                    scope.launch {
                        onClick.invoke(
                            LibraryUiEvent.ItemClick.ArtistClick(
                                id = artistPrev[it].id,
                                name = artistPrev[it].name
                            )
                        )
                    }
                }
            )
    }
}


fun LazyGridScope.libraryScreenItemHeading(
    heading: String = "Playlist",
    isGrid: Boolean,
    gridSpan: GridItemSpan = GridItemSpan(if (isGrid) 3 else 1),
    onClick: () -> Unit
) {
    item(
        span = { gridSpan }
    ) {
        Row {
            Row(
                modifier = Modifier.fillMaxWidth(.7f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = heading,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Black,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                LibraryScreenAddButton(
                    onClick = onClick
                )
            }
        }
    }
}


fun LazyGridScope.headLineSeparator(
    isGrid: Boolean,
    gridSpan: GridItemSpan = GridItemSpan(if (isGrid) 3 else 1)
) {
    item(
        span = { gridSpan }
    ) {
        LibraryScreenItemSeparationLine()
    }

    item(
        span = { gridSpan }
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
    }
}

fun LazyGridScope.largeSpace(
    gridSpan: GridItemSpan
) {
    item(
        span = { gridSpan }
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
    }
}

@Composable
fun LibraryScreenPlaylistGridView(
    modifier: Modifier = Modifier,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    shape: Shape = MaterialTheme.shapes.small,
    isCookie: Boolean,
    authHeader: String,
    name: String,
    imageUrls: List<String>
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = modifier,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = shape
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (imageUrls.size < 4)
                    CustomImageView(
                        modifier = Modifier.fillMaxSize(),
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        url = imageUrls[0],
                        contentScale = ContentScale.FillBounds
                    )
                else
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.5f)
                    ) {
                        CustomImageView(
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight(),
                            isDarkThem = isDarkThem,
                            isCookie = isCookie,
                            headerValue = authHeader,
                            url = imageUrls[0],
                            contentScale = ContentScale.FillBounds
                        )

                        CustomImageView(
                            modifier = Modifier.fillMaxSize(),
                            isDarkThem = isDarkThem,
                            isCookie = isCookie,
                            headerValue = authHeader,
                            url = imageUrls[1],
                            contentScale = ContentScale.FillBounds
                        )
                    }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CustomImageView(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(),
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        url = imageUrls[2],
                        contentScale = ContentScale.FillBounds
                    )

                    CustomImageView(
                        modifier = Modifier.fillMaxSize(),
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        url = imageUrls[3],
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        Row {
            Text(
                text = name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }
    }
}

@Composable
fun LibraryScreenPlaylistListView(
    modifier: Modifier = Modifier,
    isCookie: Boolean,
    authHeader: String,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    shape: Shape = RoundedCornerShape(0.dp),
    name: String,
    imageUrls: List<String>,
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .clip(MaterialTheme.shapes.small)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.primary
                    )
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = shape
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (imageUrls.size < 4) {
                    CustomImageView(
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        url = imageUrls[0]
                    )
                } else
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.2f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(.5f)
                        ) {
                            CustomImageView(
                                modifier = Modifier
                                    .fillMaxWidth(.5f)
                                    .fillMaxHeight(),
                                isDarkThem = isDarkThem,
                                isCookie = isCookie,
                                headerValue = authHeader,
                                url = imageUrls[0],
                            )

                            CustomImageView(
                                modifier = Modifier.fillMaxSize(),
                                isDarkThem = isDarkThem,
                                isCookie = isCookie,
                                headerValue = authHeader,
                                url = imageUrls[1],
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            CustomImageView(
                                modifier = Modifier
                                    .fillMaxWidth(.5f)
                                    .fillMaxHeight(),
                                isDarkThem = isDarkThem,
                                isCookie = isCookie,
                                headerValue = authHeader,
                                url = imageUrls[2],
                            )

                            CustomImageView(
                                modifier = Modifier.fillMaxSize(),
                                isDarkThem = isDarkThem,
                                isCookie = isCookie,
                                headerValue = authHeader,
                                url = imageUrls[3],
                            )
                        }
                    }

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                Text(
                    text = name,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryScreenAlbumAndArtistGridView(
    modifier: Modifier,
    isAlbum: Boolean,
    name: String,
    imageUrl: String,
    isCookie: Boolean,
    headerValue: String,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    shape: Shape = if (isAlbum) MaterialTheme.shapes.small else CircleShape,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = modifier,
            shape = shape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = background
            )
        ) {
            CustomImageView(
                modifier = Modifier.fillMaxSize(),
                isDarkThem = isDarkThem,
                url = imageUrl,
                isCookie = isCookie,
                headerValue = headerValue,
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        Text(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryScreenAlbumAndArtistListView(
    modifier: Modifier,
    isAlbum: Boolean,
    name: String,
    imageUrl: String,
    isCookie: Boolean,
    headerValue: String,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    shape: Shape = if (isAlbum) MaterialTheme.shapes.small else CircleShape,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = modifier,
            shape = shape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = background
            )
        ) {
            CustomImageView(
                modifier = Modifier.fillMaxSize(),
                isDarkThem = isDarkThem,
                url = imageUrl,
                isCookie = isCookie,
                headerValue = headerValue,
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

        Text(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouritePrev(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.primary
                    )
                )
            )
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.favourite),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.small2)
                        .fillMaxSize(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

        Text(
            text = "Favourites",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreenBottomSheet(
    sheetState: SheetState,
    pinnedData: PinnedData,
    onClick: (LibraryUiEvent.BottomSheetItemClick) -> Unit,
    cancelClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = cancelClick,
        sheetState = sheetState,
        properties = ModalBottomSheetDefaults.properties(
            isFocusable = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = pinnedData.name.uppercase(),
                textAlign = TextAlign.Start,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            OutlinedButton(
                onClick = {
                    onClick.invoke(
                        LibraryUiEvent.BottomSheetItemClick.AddClick(
                            type = pinnedData.type,
                            name = pinnedData.name
                        )
                    )
                },
                enabled = !pinnedData.isPinned
            ) {
                Icon(
                    modifier = Modifier
                        .rotate(45f),
                    painter = painterResource(id = R.drawable.pin),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                Text(text = "Add to pinned")
            }

            OutlinedButton(
                onClick = {
                    onClick.invoke(LibraryUiEvent.BottomSheetItemClick.RemoveClick)
                },
                enabled = pinnedData.isPinned
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                Text(text = "Remove from pinned")
            }

            OutlinedButton(
                onClick = {
                    onClick.invoke(LibraryUiEvent.BottomSheetItemClick.DeleteClick)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                if (pinnedData.type == PinnedDataType.FAVOURITE) Text(text = "Delete [${pinnedData.name.uppercase()}]")
                else Text(text = "Delete ${pinnedData.type.title}  [${pinnedData.name.uppercase()}]")
            }
        }
    }
}


@Composable
fun LibraryScreenItemSeparationLine(
    weight: Float = .97f,
    height: Dp = 1.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(weight)
                .wrapContentHeight()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
            )
        }
    }
}


@Composable
fun LibraryScreenAddButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null
        )
    }
}


@Composable
fun CustomFilterChip(
    isSelected: Boolean,
    @DrawableRes
    icon: Int,
    type: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text = type) },
        leadingIcon = {
            if (isSelected)
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
        }
    )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}