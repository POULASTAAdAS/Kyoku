package com.poulastaa.kyoku.presentation.screen.song_view.artist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.song_view.AlbumType
import com.poulastaa.kyoku.data.model.screens.song_view.AllArtistSongType
import com.poulastaa.kyoku.data.model.screens.song_view.ArtistAllUiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.BottomSheetData
import com.poulastaa.kyoku.presentation.common.ClickableItemWithDrawableImage
import com.poulastaa.kyoku.presentation.common.ClickableItemWithVectorImage
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistAllBottomSheet(
    data: BottomSheetData,
    sheetState: SheetState,
    onClick: (ArtistAllUiEvent) -> Unit,
    onCancelClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onCancelClick,
        sheetState = sheetState,
        properties = ModalBottomSheetDefaults.properties(
            isFocusable = false
        ),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                CustomImageView(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.extraSmall),
                    isDarkThem = false,
                    isCookie = false,
                    headerValue = "",
                    url = data.url
                )

                Text(
                    text = data.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

            when (data.type) {
                BottomSheetData.BottomSheetDataType.ALBUM -> {
                    ClickableItemWithDrawableImage(
                        text = "Play album",
                        icon = R.drawable.ic_play,
                        onClick = {
                            onClick.invoke(
                                ArtistAllUiEvent.BottomSheetItemClick.AlbumClick(
                                    id = data.id,
                                    name = data.name,
                                    type = AlbumType.PLAY_ALBUM
                                )
                            )
                        }
                    )

                    ClickableItemWithDrawableImage(
                        text = "Add as Playlist",
                        icon = R.drawable.ic_add_to_playlist,
                        onClick = {
                            onClick.invoke(
                                ArtistAllUiEvent.BottomSheetItemClick.AlbumClick(
                                    id = data.id,
                                    name = data.name,
                                    type = AlbumType.ADD_AS_PLAYLIST
                                )
                            )
                        }
                    )

                    if (data.operation)
                        ClickableItemWithDrawableImage(
                            text = "Remove From Library Albums",
                            icon = R.drawable.ic_remove_from_library,
                            onClick = {
                                onClick.invoke(
                                    ArtistAllUiEvent.BottomSheetItemClick.AlbumClick(
                                        id = data.id,
                                        name = data.name,
                                        type = AlbumType.REMOVE_FROM_LIBRARY_ALBUM
                                    )
                                )
                            }
                        )
                    else
                        ClickableItemWithDrawableImage(
                            text = "Add to Library Albums",
                            icon = R.drawable.ic_add_to_library,
                            onClick = {

                                onClick.invoke(
                                    ArtistAllUiEvent.BottomSheetItemClick.AlbumClick(
                                        id = data.id,
                                        name = data.name,
                                        type = AlbumType.ADD_TO_LIBRARY_ALBUM
                                    )
                                )
                            }
                        )

                    ClickableItemWithDrawableImage(
                        text = "Download Album",
                        icon = R.drawable.ic_download,
                        onClick = {

                            onClick.invoke(
                                ArtistAllUiEvent.BottomSheetItemClick.AlbumClick(
                                    id = data.id,
                                    name = data.name,
                                    type = AlbumType.DOWNLOAD_ALBUM
                                )
                            )
                        }
                    )
                }

                BottomSheetData.BottomSheetDataType.SONG -> {
                    ClickableItemWithDrawableImage(
                        text = "Play Song",
                        icon = R.drawable.ic_play,
                        onClick = {
                            onClick.invoke(
                                ArtistAllUiEvent.BottomSheetItemClick.SongClick(
                                    id = data.id,
                                    name = data.name,
                                    type = AllArtistSongType.PLAY_SONG
                                )
                            )
                        }
                    )

                    if (data.operation)
                        ClickableItemWithDrawableImage(
                            text = "Remove form favourite",
                            icon = R.drawable.ic_remove_favourite,
                            onClick = {
                                onClick.invoke(
                                    ArtistAllUiEvent.BottomSheetItemClick.SongClick(
                                        id = data.id,
                                        name = data.name,
                                        type = AllArtistSongType.REMOVE_FROM_FAVOURITE
                                    )
                                )
                            }
                        )
                    else
                        ClickableItemWithVectorImage(
                            text = "Add to favourite",
                            icon = Icons.Rounded.Favorite,
                            onClick = {
                                onClick.invoke(
                                    ArtistAllUiEvent.BottomSheetItemClick.SongClick(
                                        id = data.id,
                                        name = data.name,
                                        type = AllArtistSongType.ADD_TO_FAVOURITE
                                    )
                                )
                            }
                        )

                    ClickableItemWithDrawableImage(
                        text = "Add to Playlist",
                        icon = R.drawable.ic_add_to_playlist,
                        onClick = {
                            onClick.invoke(
                                ArtistAllUiEvent.BottomSheetItemClick.SongClick(
                                    id = data.id,
                                    name = data.name,
                                    type = AllArtistSongType.ADD_TO_PLAYLIST
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}