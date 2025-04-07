package com.poulastaa.view.presentation.saved

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.view.presentation.components.LoadingSongs
import com.poulastaa.view.presentation.saved.components.AddNewItemCard
import com.poulastaa.view.presentation.saved.components.ViewSavedItemImageCard
import com.poulastaa.view.presentation.saved.components.ViewSavedItemTopBar
import com.poulastaa.view.presentation.saved.components.ViewStavedItemExtendedLoadingCommonContent

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
                    ViewSavedItemTopBar(
                        isEditEnabled = state.isEditEnabled,
                        type = state.type,
                        navigateBack = navigateBack,
                        onAction = onAction
                    )
                }

                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                item {
                    AddNewItemCard(
                        Modifier.fillMaxSize(.5f),
                        state.isEditEnabled,
                        state.type,
                        onAction
                    )
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
                        ViewSavedItemImageCard(
                            Modifier.aspectRatio(1f),
                            state.type,
                            item.poster
                        )

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
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = when {
                                    item.artist != null && state.type != ViewSavedUiItemType.ARTIST -> item.artist
                                    item.releaseYear != null && state.type != ViewSavedUiItemType.ARTIST -> "Year: ${item.releaseYear}"
                                    else -> if (state.type == ViewSavedUiItemType.ARTIST) "followers: ${item.numbers}"
                                    else "songs: ${item.numbers}"
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            )
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

            LoadingType.Loading -> ViewStavedItemExtendedLoadingCommonContent(
                it,
                state.type,
                navigateBack
            ) {
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