package com.poulastaa.view.presentation.saved

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.ui.DeleteIcon
import com.poulastaa.core.presentation.designsystem.ui.EditIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.view.presentation.components.LoadingSongs

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ViewSavedItemCompactScreen(
    state: ViewSavedUiState,
    onAction: (ViewSavedUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        when (state.loadingType) {
            LoadingType.Content -> LazyColumn(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(it),
            ) {
                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium3))
                }

                item {
                    AppBasicTopBar(
                        title = "${stringResource(R.string.save)}d ${stringResource(state.type.value)}s",
                        navigateBack = navigateBack,
                        actions = {
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = if (state.isEditEnabled) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.primary,
                                    containerColor = if (state.isEditEnabled) MaterialTheme.colorScheme.errorContainer
                                    else Color.Transparent
                                ),
                                onClick = {

                                },
                                content = {
                                    AnimatedContent(targetState = state.isEditEnabled) {
                                        when (it) {
                                            true -> Icon(
                                                imageVector = DeleteIcon,
                                                contentDescription = stringResource(R.string.delete)
                                            )

                                            false -> Icon(
                                                imageVector = EditIcon,
                                                contentDescription = stringResource(id = R.string.edit)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    )
                }

                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                item {
                    AnimatedVisibility(state.isEditEnabled.not()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(.5f),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 5.dp,
                                    pressedElevation = 0.dp
                                ),
                                border = BorderStroke(
                                    width = 1.4.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                shape = CircleShape,
                                onClick = {
                                    onAction(ViewSavedUiAction.OnAddNewItemClick)
                                }
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (state.type) {
                                            ViewSavedUiItemType.ARTIST -> stringResource(R.string.add_new_artist)
                                            ViewSavedUiItemType.PLAYLIST -> stringResource(R.string.add_new_playlist)
                                            ViewSavedUiItemType.ALBUM -> stringResource(R.string.add_new_album)
                                            ViewSavedUiItemType.NONE -> throw IllegalArgumentException(
                                                "Wrong ViewSavedUiItem Type"
                                            )
                                        },
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(MaterialTheme.dimens.small2 + MaterialTheme.dimens.small1)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                items(state.items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.dimens.medium1)
                            .height(80.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .combinedClickable(
                                onClick = {
                                    onAction(
                                        ViewSavedUiAction.OnItemClick(
                                            item.id,
                                            ItemClickType.CLICK
                                        )
                                    )
                                },
                                onLongClick = {
                                    onAction(
                                        ViewSavedUiAction.OnItemClick(
                                            item.id,
                                            ItemClickType.LONG_CLICK
                                        )
                                    ).also {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier.aspectRatio(1f),
                            shape = if (state.type == ViewSavedUiItemType.ARTIST) CircleShape
                            else MaterialTheme.shapes.extraSmall,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            if (state.type == ViewSavedUiItemType.ARTIST) {

                            } else SubcomposeAsyncImage(
                                model = CacheImageReq.imageReq(
                                    item.poster.first(),
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
                                            color = MaterialTheme.colorScheme.primary
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
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            )
                        }

                        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            when {
                                item.artist != null && state.type != ViewSavedUiItemType.ARTIST -> Text(
                                    text = item.artist,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                )

                                item.releaseYear != null && state.type != ViewSavedUiItemType.ARTIST -> Text(
                                    text = "Year: ${item.releaseYear}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                )

                                else -> Text(
                                    text = if (state.type == ViewSavedUiItemType.ARTIST) "followers: ${item.numbers}"
                                    else "songs: ${item.numbers}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                )
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        Icon(
                            imageVector = ArrowDownIcon,
                            contentDescription = stringResource(R.string.all),
                            modifier = Modifier
                                .rotate(-90f)
                                .noRippleClickable {
                                    onAction(
                                        ViewSavedUiAction.OnItemClick(
                                            item.id,
                                            ItemClickType.CLICK
                                        )
                                    ).also {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                }
                        )
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.small1))
                }

                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }
            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingType,
                navigateBack = navigateBack
            )

            LoadingType.Loading -> {
                Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                        )
                        .fillMaxSize()
                        .padding(it)
                        .padding(MaterialTheme.dimens.medium1)
                        .navigationBarsPadding()
                        .systemBarsPadding()
                        .verticalScroll(rememberScrollState()),
                ) {
                    AppBasicTopBar(
                        title = "${stringResource(R.string.save)}d ${stringResource(state.type.value)}s",
                        navigateBack = navigateBack,
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .height(48.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            ),
                            shape = CircleShape
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .shimmerEffect(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    LoadingSongs(
                        itemCount = 10,
                        isCircularItem = state.type == ViewSavedUiItemType.ARTIST,
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewSavedItemCompactScreen(
                state = ViewSavedUiState(
                    loadingType = LoadingType.Content,
                    type = ViewSavedUiItemType.ARTIST,
                    items = (1..10).map {
                        ViewSavedUiItem(
                            id = it.toLong(),
                            title = "That Cool Artist",
                            poster = emptyList(),
                            artist = "That Cool Artist",
                            releaseYear = 2024,
                            numbers = 10
                        )
                    }
                ),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}