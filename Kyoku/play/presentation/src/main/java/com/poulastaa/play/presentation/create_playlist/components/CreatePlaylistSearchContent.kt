package com.poulastaa.play.presentation.create_playlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.poulastaa.core.domain.model.CreatePlaylistPagerFilterType
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.presentation.designsystem.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.FilterPlaylistIcon
import com.poulastaa.core.presentation.designsystem.MoreFromArtistIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppFilterChip
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistPagingUiData
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistUiEvent
import com.poulastaa.play.presentation.create_playlist.toUiSong

@Composable
fun CreatePlaylistSearchContent(
    header: String,
    filterType: CreatePlaylistPagerFilterType,
    data: LazyPagingItems<CreatePlaylistPagingUiData>,
    onEvent: (CreatePlaylistUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.dimens.medium1)
    ) {
        Spacer(Modifier.height(MaterialTheme.dimens.small1))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            AppFilterChip(
                text = stringResource(R.string.all),
                shape = MaterialTheme.shapes.medium,
                selected = filterType == CreatePlaylistPagerFilterType.ALL,
                icon = MoreFromArtistIcon,
                onClick = {
                    onEvent(CreatePlaylistUiEvent.OnFilterTypeChange(CreatePlaylistPagerFilterType.ALL))
                }
            )

            AppFilterChip(
                text = stringResource(R.string.artist),
                shape = MaterialTheme.shapes.medium,
                selected = filterType == CreatePlaylistPagerFilterType.ARTIST,
                icon = FilterArtistIcon,
                onClick = {
                    onEvent(CreatePlaylistUiEvent.OnFilterTypeChange(CreatePlaylistPagerFilterType.ARTIST))
                }
            )

            AppFilterChip(
                text = stringResource(R.string.album),
                shape = MaterialTheme.shapes.medium,
                selected = filterType == CreatePlaylistPagerFilterType.ALBUM,
                icon = FilterAlbumIcon,
                onClick = {
                    onEvent(CreatePlaylistUiEvent.OnFilterTypeChange(CreatePlaylistPagerFilterType.ALBUM))
                }
            )

            AppFilterChip(
                text = stringResource(R.string.playlist),
                shape = MaterialTheme.shapes.medium,
                selected = filterType == CreatePlaylistPagerFilterType.PLAYLIST,
                icon = FilterPlaylistIcon,
                onClick = {
                    onEvent(CreatePlaylistUiEvent.OnFilterTypeChange(CreatePlaylistPagerFilterType.PLAYLIST))
                }
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Card(
            modifier = Modifier
                .fillMaxSize(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.small
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                contentPadding = PaddingValues(vertical = MaterialTheme.dimens.medium1)
            ) {
                items(data.itemCount) { index ->
                    data[index]?.let { data ->
                        if (data.isArtist) {
                            CreatePlaylistArtistCard(
                                header = header,
                                data = data,
                                modifier = Modifier
                                    .clickable {
                                        onEvent(
                                            CreatePlaylistUiEvent.OnArtistClick(
                                                artistId = data.id
                                            )
                                        )
                                    }
                                    .padding(MaterialTheme.dimens.small1)
                            )
                        } else if (data.expandable) {
                            CreatePlaylistAlbumCard(
                                header = header,
                                data = data,
                                modifier = Modifier
                                    .clickable {
                                        onEvent(
                                            CreatePlaylistUiEvent.OnAlbumClick(
                                                albumId = data.id
                                            )
                                        )
                                    }
                                    .padding(MaterialTheme.dimens.small1)
                            )
                        } else {
                            CreatePlaylistSongCard(
                                header = header,
                                song = data.toUiSong(),
                                modifier = Modifier
                                    .clickable {
                                        onEvent(
                                            CreatePlaylistUiEvent.OnSongClick(
                                                type = CreatePlaylistType.SEARCH,
                                                songId = data.id
                                            )
                                        )
                                    }
                                    .padding(MaterialTheme.dimens.small1)
                            )
                        }
                    }
                }
            }
        }
    }
}