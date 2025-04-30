package com.poulastaa.add.presentation.album.view_saved

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.add.presentation.album.AddAlbumUiAction
import com.poulastaa.add.presentation.album.UiAlbum
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.ui.DeleteIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewSavedBottomBar(
    list: List<UiAlbum>,
    onAction: (AddAlbumUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    ModalBottomSheet(
        onDismissRequest = {
            onAction(AddAlbumUiAction.OnViewSelectedToggle)
        },
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = {
            Box(
                Modifier
                    .padding(MaterialTheme.dimens.medium1)
                    .clip(CircleShape)
                    .fillMaxWidth(.3f)
                    .height(5.dp)
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(MaterialTheme.dimens.medium1)
                .padding(bottom = MaterialTheme.dimens.medium1)
        ) {
            list.forEach { album ->
                Box(
                    Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraSmall)
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.error)
                    )

                    SlidableAlbumCard(haptic, album, onAction)
                }

                Spacer(Modifier.height(MaterialTheme.dimens.small2))
            }
        }
    }
}

@Composable
private fun BoxScope.SlidableAlbumCard(
    haptic: HapticFeedback,
    album: UiAlbum,
    onAction: (AddAlbumUiAction) -> Unit,
) {
    var dragScope by remember { mutableFloatStateOf(0f) }
    var sliderMaxWidth by remember { mutableIntStateOf(0) }

    val targetAngle = if (dragScope.absoluteValue >= sliderMaxWidth) 45f
    else 0f
    val rotationAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(durationMillis = 400),
        label = "deleteIconRotation"
    )


    Card(
        modifier = Modifier
            .padding(start = MaterialTheme.dimens.small3)
            .fillMaxHeight(.5f)
            .aspectRatio(1f)
            .align(Alignment.CenterStart),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        shape = CircleShape
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = DeleteIcon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(.7f)
                    .rotate(-rotationAngle)
            )
        }
    }

    Card(
        modifier = Modifier
            .padding(end = MaterialTheme.dimens.small3)
            .fillMaxHeight(.5f)
            .aspectRatio(1f)
            .align(Alignment.CenterEnd),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        shape = CircleShape
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = DeleteIcon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(.7f)
                    .rotate(rotationAngle)
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                sliderMaxWidth = (it.size.width / 3)
            }
            .offset {
                IntOffset(dragScope.toInt(), 0)
            }
            .draggable(
                state = rememberDraggableState { delta ->
                    dragScope += delta
                },
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    if (dragScope.absoluteValue >= sliderMaxWidth) onAction(
                        AddAlbumUiAction.OnAlbumClick(
                            album = album,
                            clickType = AddAlbumUiAction.ClickType.EDIT
                        )
                    ).also {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }

                    dragScope = 0f
                }
            )
            .background(MaterialTheme.colorScheme.primaryContainer)
            .align(Alignment.Center),
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
                .fillMaxWidth(.8f),
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


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewSavedBottomBar(
                list = (1..10).map {
                    UiAlbum(
                        name = "That Cool Album",
                        artist = "That Cool Artist",
                    )
                },
                onAction = {}
            )
        }
    }
}