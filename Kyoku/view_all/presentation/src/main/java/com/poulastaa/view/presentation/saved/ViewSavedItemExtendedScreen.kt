package com.poulastaa.view.presentation.saved

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorExpandedScreen
import com.poulastaa.view.presentation.saved.components.AddNewItemCard
import com.poulastaa.view.presentation.saved.components.ViewSavedItemImageCard
import com.poulastaa.view.presentation.saved.components.ViewSavedItemTopBar
import com.poulastaa.view.presentation.saved.components.ViewStavedItemExtendedLoadingCommonContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ViewSavedItemExtendedScreen(
    state: ViewSavedUiState,
    onAction: (ViewSavedUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        when (state.loadingType) {
            LoadingType.Content -> LazyVerticalGrid(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(it),
                columns = GridCells.Fixed(6),
                contentPadding = PaddingValues(
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1,
                    bottom = MaterialTheme.dimens.medium1
                )
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    ViewSavedItemTopBar(
                        isEditEnabled = state.isEditEnabled,
                        type = state.type,
                        navigateBack = {
                            if (state.isEditEnabled) onAction(ViewSavedUiAction.OnClearSelectedDialogToggle)
                            else navigateBack()
                        },
                        onAction = onAction
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    AddNewItemCard(
                        Modifier.fillMaxSize(.35f),
                        state.isEditEnabled,
                        state.type,
                        onAction
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                items(state.items) { item ->
                    Column(
                        modifier = Modifier
                            .aspectRatio(1f)
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
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(.8f)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
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
                                            )
                                        }
                                    ),
                                shape = if (state.type == ViewSavedUiItemType.ARTIST) CircleShape
                                else MaterialTheme.shapes.extraSmall,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 0.dp
                                )
                            ) {
                                ViewSavedItemImageCard(
                                    Modifier.fillMaxSize(),
                                    state.type,
                                    item.poster
                                )
                            }

                            this@Column.AnimatedVisibility(
                                state.isEditEnabled,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                            ) {
                                Checkbox(
                                    checked = state.selectedList.contains(item.id),
                                    onCheckedChange = {
                                        onAction(ViewSavedUiAction.OnSelectToggle(item.id))
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                        uncheckedColor = MaterialTheme.colorScheme.primary,
                                        checkmarkColor = MaterialTheme.colorScheme.background
                                    )
                                )
                            }
                        }



                        Spacer(Modifier.height(MaterialTheme.dimens.small1))

                        Column(
                            modifier = Modifier.weight(.18f),
                        ) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            is LoadingType.Error -> AppErrorExpandedScreen(
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

                repeat(5) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        repeat(6) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Card(
                                    modifier = Modifier
                                        .weight(.8f)
                                        .aspectRatio(1f),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 5.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Box(
                                        Modifier
                                            .fillMaxSize()
                                            .shimmerEffect(MaterialTheme.colorScheme.primary)
                                    )
                                }

                                Spacer(Modifier.height(MaterialTheme.dimens.small1))

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(.8f)
                                        .weight(.12f),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 5.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    shape = MaterialTheme.shapes.extraSmall
                                ) {
                                    Box(
                                        Modifier
                                            .fillMaxSize()
                                            .shimmerEffect(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.small3))
                }

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))
            }
        }
    }
}

@Preview(
    widthDp = 1024,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 1280,
    heightDp = 740
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewSavedItemExtendedScreen(
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
                    },
                    selectedList = listOf(1, 2)
                ),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}