package com.poulastaa.play.presentation.root_drawer.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poulastaa.core.presentation.designsystem.AddAsPlaylistIcon
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.DownloadIcon
import com.poulastaa.core.presentation.designsystem.FavouriteIcon
import com.poulastaa.core.presentation.designsystem.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.FilterPlaylistIcon
import com.poulastaa.core.presentation.designsystem.FollowArtistIcon
import com.poulastaa.core.presentation.designsystem.NotFavouriteIcon
import com.poulastaa.core.presentation.designsystem.PlayIcon
import com.poulastaa.core.presentation.designsystem.PlayLastIcon
import com.poulastaa.core.presentation.designsystem.PlayNextIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UnFollowArtistIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReqSongCover
import com.poulastaa.play.presentation.root_drawer.home.HomeUiEvent
import com.poulastaa.play.presentation.root_drawer.home.model.ItemBottomSheetUiState
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import com.poulastaa.play.presentation.root_drawer.model.HomeItemClickType
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemBottomSheet(
    sheetState: SheetState,
    header: String,
    state: ItemBottomSheetUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onCancel: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
        properties = ModalBottomSheetDefaults.properties(
            isFocusable = false,
        ),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(
            topStart = MaterialTheme.dimens.medium1,
            topEnd = MaterialTheme.dimens.medium1
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
        ) {
            if (state.isBottomSheetLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.medium1)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                ) {
                    if (state.urls.size > 1) ImageGrid(
                        modifier = Modifier.aspectRatio(1f),
                        header = header,
                        urls = state.urls,
                        color = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(.4f)
                        ),
                        shapes = MaterialTheme.shapes.extraSmall
                    )
                    else Card(
                        modifier = Modifier.aspectRatio(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(.7f)
                        ),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        AsyncImage(
                            model = imageReqSongCover(
                                header = header,
                                url = state.urls.firstOrNull() ?: ""
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

                    Text(
                        text = state.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(1.5.dp)
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(.8f))
                )

                when (state.itemType) {
                    HomeItemClickType.POPULAR_SONG_MIX -> {
                        Option(
                            text = "${stringResource(id = R.string.play)} ${state.title}",
                            icon = PlayIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.PlayPopularSongMix)
                        }

                        Option(
                            text = stringResource(id = R.string.add_as_playlist),
                            icon = AddAsPlaylistIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.AddAsPlaylistPopularSongMix)
                        }

                        Option(
                            text = "${stringResource(id = R.string.download)} ${state.title}",
                            icon = DownloadIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.DownloadPopularSongMix)
                        }
                    }

                    HomeItemClickType.OLD_GEM -> {
                        Option(
                            text = "${stringResource(id = R.string.play)} ${state.title}",
                            icon = PlayIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.PlayOldGem)
                        }

                        Option(
                            text = stringResource(id = R.string.add_as_playlist),
                            icon = AddAsPlaylistIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.AddAsPlaylistOldGem)
                        }

                        Option(
                            text = "${stringResource(id = R.string.download)} ${state.title}",
                            icon = DownloadIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.DownloadOldGem)
                        }
                    }

                    HomeItemClickType.FAVOURITE_ARTIST_MIX -> {
                        Option(
                            text = "${stringResource(id = R.string.play)} ${state.title}",
                            icon = PlayIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.PlayFavouriteArtistMix)
                        }

                        Option(
                            text = stringResource(id = R.string.add_as_playlist),
                            icon = AddAsPlaylistIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.AddAsFavouriteArtistMix)
                        }

                        Option(
                            text = "${stringResource(id = R.string.download)} ${state.title}",
                            icon = DownloadIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.DownloadFavouriteArtistMix)
                        }
                    }

                    HomeItemClickType.SUGGEST_ARTIST -> {
                        if (state.flag) Option(
                            text = "${stringResource(id = R.string.un_follow)} ${state.title}",
                            icon = UnFollowArtistIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.UnFollowArtist(
                                    state.id ?: -1L
                                )
                            )
                        } else Option(
                            text = "${stringResource(id = R.string.follow)} ${state.title}",
                            icon = FollowArtistIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.FollowArtist(
                                    state.id ?: -1L
                                )
                            )
                        }


                        Option(
                            text = "${stringResource(id = R.string.explore)} Artist",
                            icon = FilterArtistIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.ExploreArtist(
                                    state.id ?: -1L
                                )
                            )
                        }
                    }

                    HomeItemClickType.SUGGEST_ALBUM -> {
                        Option(
                            text = "${stringResource(id = R.string.play)} ${state.title}",
                            icon = PlayIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.PlayAlbum(state.id ?: -1L))
                        }

                        if (state.flag) Option(
                            text = stringResource(id = R.string.remove_from_saved_album),
                            icon = CancelIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.RemoveSavedAlbum(
                                    state.id ?: -1L
                                )
                            )
                        } else Option(
                            text = stringResource(id = R.string.save_album),
                            icon = FilterAlbumIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.SaveAlbum(state.id ?: -1L))
                        }
                    }

                    HomeItemClickType.SUGGEST_ARTIST_SONG -> {
                        if (!state.flag) Option( // if nothing playing then play
                            text = "${stringResource(id = R.string.play)} ${state.title}",
                            icon = PlayIcon
                        ) {
                            onEvent(HomeUiEvent.ItemBottomSheetUiEvent.PlaySong(state.id ?: -1L))
                        } else if (state.isQueue) Option(
                            text = stringResource(id = R.string.remove_from_queue),
                            icon = CancelIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.RemoveFromQueue(
                                    state.id ?: -1L
                                )
                            )

                        } else {
                            Option(
                                text = stringResource(id = R.string.play_next_on_queue),
                                icon = PlayNextIcon
                            ) {
                                onEvent(
                                    HomeUiEvent.ItemBottomSheetUiEvent.PlayNextOnQueue(
                                        state.id ?: -1L
                                    )
                                )
                            }

                            Option(
                                text = stringResource(id = R.string.play_last_on_queue),
                                icon = PlayLastIcon
                            ) {
                                onEvent(
                                    HomeUiEvent.ItemBottomSheetUiEvent.PlayLastOnQueue(
                                        state.id ?: -1L
                                    )
                                )
                            }
                        }

                        if (state.isInFavourite) Option(
                            text = stringResource(id = R.string.remove_from_favourite),
                            icon = NotFavouriteIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.RemoveSongToFavourite(
                                    state.id ?: -1L
                                )
                            )
                        }
                        else Option(
                            text = stringResource(id = R.string.add_to_favourite),
                            icon = FavouriteIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.AddSongToFavourite(
                                    state.id ?: -1L
                                )
                            )
                        }

                        Option(
                            text = stringResource(id = R.string.add_to_playlist),
                            icon = FilterPlaylistIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.AddSongToPlaylist(
                                    id = state.id ?: -1L,
                                    artistId = state.otherId
                                )
                            )
                        }

                        Option(
                            text = stringResource(id = R.string.view_artist),
                            icon = FilterArtistIcon
                        ) {
                            onEvent(
                                HomeUiEvent.ItemBottomSheetUiEvent.ViewSongArtist(
                                    state.id ?: -1L
                                )
                            )
                        }
                    }

                    else -> Unit
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            }
        }
    }
}

@Composable
fun Option(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable {
                onClick()
            }
            .padding(vertical = MaterialTheme.dimens.small1)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Text(text = text, maxLines = 1)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    val state = rememberModalBottomSheetState()

    var bool by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = Unit) {
        delay(1000)
        bool = false
    }

    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ItemBottomSheet(
                sheetState = state,
                header = "",
                state = ItemBottomSheetUiState(
                    isOpen = true,
                    title = "Title",
                    isBottomSheetLoading = bool,
                    itemType = HomeItemClickType.SUGGEST_ARTIST_SONG,
                    flag = false,
                    isQueue = true,
                    isInFavourite = false
                ),
                onEvent = {}
            ) {

            }
        }
    }
}