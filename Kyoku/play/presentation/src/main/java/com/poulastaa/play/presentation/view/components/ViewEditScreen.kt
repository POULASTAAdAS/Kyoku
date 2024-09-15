package com.poulastaa.play.presentation.view.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.ViewUiSong
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import com.poulastaa.play.presentation.view.ViewUiData
import com.poulastaa.play.presentation.view.ViewUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEditScreen(
    modifier: Modifier = Modifier,
    header: String,
    data: ViewUiData,
    onEvent: (ViewUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scroll,
                title = {
                    Text(
                        text = data.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.inversePrimary
                    )
                },
                navigationIcon = {
                    AppBackButton(
                        icon = CancelIcon,
                        onClick = navigateBack
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.inversePrimary
                        ),
                    )
                )
                .padding(paddingValues),
            contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                FilledTonalButton(
                    onClick = {},
                    elevation = ButtonDefaults.filledTonalButtonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 3.dp
                    ),
                    modifier = Modifier.fillMaxWidth(.45f)
                ) {
                    Text(text = stringResource(R.string.explore))
                }
            }

            item {
                Spacer(Modifier.height(MaterialTheme.dimens.large1))
            }

            items(data.listOfSong) { item ->
                DraggableSongCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    haptic = haptic,
                    header = header,
                    song = item
                )

                Spacer(Modifier.height(MaterialTheme.dimens.small3))
            }
        }
    }
}

@Composable
fun DraggableSongCard(
    modifier: Modifier = Modifier,
    haptic: HapticFeedback,
    header: String,
    song: ViewUiSong,
) {
    var dragOffSetX by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(dragOffSetX < -350f) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Log.d("drag", dragOffSetX.toString())

    Box(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = MaterialTheme.shapes.extraSmall,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFD27474)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onError)
                        .padding(MaterialTheme.dimens.small2)
                        .rotate(if (dragOffSetX < -350f) -45f else 0f),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        dragOffSetX
                            .coerceAtMost(0f)
                            .toInt(), 0
                    )
                }
                .draggable(
                    state = rememberDraggableState {
                        dragOffSetX += it
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (dragOffSetX < -350f) {

                        } else dragOffSetX =  0f
                    }
                ),
            shape = MaterialTheme.shapes.extraSmall,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.small2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageGrid(
                    header = header,
                    urls = listOf(song.coverImage),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = song.name,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = song.artist,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onBackground.copy(.6f)
                    )
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        ViewEditScreen(
            data = ViewUiData(
                name = "Playlist",
                listOfSong = (1..10).map {
                    ViewUiSong(
                        id = it.toLong(),
                        name = "Song $it",
                        artist = "Artist $it"
                    )
                }
            ),
            header = "",
            onEvent = {}
        ) { }
    }
}