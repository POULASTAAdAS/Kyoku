package com.poulastaa.play.presentation.root_drawer.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.DownloadIcon
import com.poulastaa.core.presentation.designsystem.FavouriteIcon
import com.poulastaa.core.presentation.designsystem.PinIcon
import com.poulastaa.core.presentation.designsystem.PlayIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UnPinIcon
import com.poulastaa.core.presentation.designsystem.ViewIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.root_drawer.home.components.Option
import com.poulastaa.play.presentation.root_drawer.library.LibraryBottomSheetLongClickType
import com.poulastaa.play.presentation.root_drawer.library.LibraryBottomSheetUiState
import com.poulastaa.play.presentation.root_drawer.library.LibraryUiEvent
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryItemBottomSheet(
    sheetState: SheetState,
    header: String,
    state: LibraryBottomSheetUiState,
    onEvent: (LibraryUiEvent.BottomSheetUiEvent) -> Unit,
    onCancel: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(
            topStart = MaterialTheme.dimens.medium1,
            topEnd = MaterialTheme.dimens.medium1
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
        ) {
            if (state.type == LibraryBottomSheetLongClickType.LOAD) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.medium1)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                if (state.type == LibraryBottomSheetLongClickType.FAVOURITE) Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(MaterialTheme.shapes.small)
                        .size(120.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    MaterialTheme.colorScheme.primary.copy(.7f)
                                )
                            )
                        ),
                ) {
                    Icon(
                        imageVector = FavouriteIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.small3),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else ImageGrid(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally),
                    header = header,
                    urls = state.urls,
                    color = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(.4f)
                    ),
                    shapes = MaterialTheme.shapes.extraSmall
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                Text(
                    text = state.title,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                HorizontalDivider()

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    when (state.type) {
                        LibraryBottomSheetLongClickType.FAVOURITE -> {
                            Option(
                                text = "${stringResource(id = R.string.play)} ${state.title}",
                                icon = PlayIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Favourite.Play(state.id))
                            }

                            if (state.isPinned) PinOption(
                                title = "${stringResource(id = R.string.un_pin)} ${state.title}",
                                icon = UnPinIcon,
                                onClick = {
                                    onEvent(LibraryUiEvent.BottomSheetUiEvent.Favourite.UnPin(state.id))
                                }
                            ) else PinOption(
                                title = "${stringResource(id = R.string.pin)} ${state.title}",
                                icon = PinIcon,
                                onClick = {
                                    onEvent(LibraryUiEvent.BottomSheetUiEvent.Favourite.Pin(state.id))
                                }
                            )

                            Option(
                                text = "${stringResource(id = R.string.view)} ${state.title}",
                                icon = ViewIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Favourite.View(state.id))
                            }

                            Option(
                                text = "${stringResource(id = R.string.download)} ${state.title}",
                                icon = DownloadIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Favourite.Download(state.id))
                            }
                        }

                        LibraryBottomSheetLongClickType.ALBUM -> {
                            Option(
                                text = "${stringResource(id = R.string.play)} ${state.title}",
                                icon = PlayIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Album.Play(state.id))
                            }

                            if (state.isPinned) PinOption(
                                title = "${stringResource(id = R.string.un_pin)} ${state.title}",
                                icon = UnPinIcon,
                                onClick = {
                                    onEvent(LibraryUiEvent.BottomSheetUiEvent.Album.UnPin(state.id))
                                }
                            ) else PinOption(
                                title = "${stringResource(id = R.string.pin)} ${state.title}",
                                icon = PinIcon,
                                onClick = {
                                    onEvent(LibraryUiEvent.BottomSheetUiEvent.Album.Pin(state.id))
                                }
                            )

                            Option(
                                text = "${stringResource(id = R.string.view)} ${state.title}",
                                icon = ViewIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Album.View(state.id))
                            }

                            Option(
                                text = "${stringResource(id = R.string.download)} ${state.title}",
                                icon = DownloadIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Album.Download(state.id))
                            }

                            Option(
                                text = "${stringResource(id = R.string.remove)} ${state.title}",
                                icon = CancelIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Album.Remove(state.id))
                            }
                        }

                        LibraryBottomSheetLongClickType.PLAYLIST -> {
                            Option(
                                text = "${stringResource(id = R.string.play)} ${state.title}",
                                icon = PlayIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Playlist.Play(state.id))
                            }

                            if (state.isPinned) PinOption(
                                title = "${stringResource(id = R.string.un_pin)} ${state.title}",
                                icon = UnPinIcon,
                                onClick = {
                                    onEvent(LibraryUiEvent.BottomSheetUiEvent.Playlist.UnPin(state.id))
                                }
                            ) else PinOption(
                                title = "${stringResource(id = R.string.pin)} ${state.title}",
                                icon = PinIcon,
                                onClick = {
                                    onEvent(LibraryUiEvent.BottomSheetUiEvent.Playlist.Pin(state.id))
                                }
                            )

                            Option(
                                text = "${stringResource(id = R.string.view)} ${state.title}",
                                icon = ViewIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Playlist.View(state.id))
                            }

                            Option(
                                text = "${stringResource(id = R.string.download)} ${state.title}",
                                icon = DownloadIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Playlist.Download(state.id))
                            }

                            Option(
                                text = "${stringResource(id = R.string.remove)} ${state.title}",
                                icon = CancelIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Playlist.Remove(state.id))
                            }
                        }

                        LibraryBottomSheetLongClickType.ARTIST -> {
                            if (state.isPinned) PinOption(
                                title = "${stringResource(id = R.string.un_pin)} ${state.title}",
                                icon = UnPinIcon,
                                onClick = {
                                    onEvent(LibraryUiEvent.BottomSheetUiEvent.Artist.UnPin(state.id))
                                }
                            ) else PinOption(
                                title = "${stringResource(id = R.string.pin)} ${state.title}",
                                icon = PinIcon,
                                onClick = {
                                    onEvent(LibraryUiEvent.BottomSheetUiEvent.Artist.Pin(state.id))

                                }
                            )

                            Option(
                                text = "${stringResource(id = R.string.view)} ${state.title}",
                                icon = ViewIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Artist.View(state.id))
                            }

                            Option(
                                text = "${stringResource(id = R.string.un_follow)} ${state.title}",
                                icon = CancelIcon
                            ) {
                                onEvent(LibraryUiEvent.BottomSheetUiEvent.Artist.UnFollow(state.id))
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun PinOption(
    title: String,
    icon: ImageVector = PinIcon,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.small1)
            .clip(CircleShape)
            .clickable {
                onClick()
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.rotate(45f)
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Text(
            text = title,
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    val sheet = rememberModalBottomSheetState()

    var type by remember {
        mutableStateOf(LibraryBottomSheetLongClickType.ARTIST)
    }

    LaunchedEffect(key1 = true) {
        delay(1000)
        type = LibraryBottomSheetLongClickType.ARTIST
    }

    AppThem {
        Column {
            LibraryItemBottomSheet(
                sheetState = sheet,
                header = "",
                state = LibraryBottomSheetUiState(
                    type = type,
                    urls = listOf(""),
                    title = "Artist",
                ),
                onEvent = {},
                onCancel = {}
            )
        }
    }
}