package com.poulastaa.add.presentation.artist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.artist.components.AddArtistCard
import com.poulastaa.add.presentation.artist.components.AddArtistLoadingFilterCard
import com.poulastaa.add.presentation.artist.components.AddArtistSearchFilterChips
import com.poulastaa.add.presentation.artist.components.LoadingArtistCard
import com.poulastaa.add.presentation.components.AddSearchTopBar
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.core.presentation.ui.components.AppLoadingSearchTopBar
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddArtistCompactScreen(
    scroll: TopAppBarScrollBehavior,
    columns: Int,
    state: AddArtistUiState,
    artist: LazyPagingItems<UiArtist>,
    onAction: (AddArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            if (state.loadingType == LoadingType.Content) AddSearchTopBar(
                scrollBehavior = scroll,
                label = stringResource(R.string.search_artist),
                query = state.query,
                isExtended = false,
                focusManager = LocalFocusManager.current,
                onValueChange = { onAction(AddArtistUiAction.OnSearchQueryChange(it)) },
                actions = {
                    AddArtistActions(
                        isSaving = state.isSaving,
                        isEditEnabled = state.isEditEnabled,
                        onAction = onAction,
                        haptic = haptic
                    )
                },
                navigateBack = {
                    if (state.query.isEmpty()) navigateBack()
                    else if (state.isEditEnabled) onAction(AddArtistUiAction.OnCancelSaveClick)
                    else onAction(AddArtistUiAction.OnSearchQueryChange(""))

                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = state.selectedArtist.isNotEmpty()) {
                Button(
                    onClick = {
                        onAction(AddArtistUiAction.OnViewSelectedToggle)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = "${state.selectedArtist.size} ${stringResource(R.string.selected)}"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        when (state.loadingType) {
            LoadingType.Loading -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1)
            ) {
                AppLoadingSearchTopBar(
                    Modifier.fillMaxWidth(),
                    title = stringResource(R.string.search_artist),
                    navigateBack = navigateBack
                )

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    repeat(2) {
                        AddArtistLoadingFilterCard()
                    }
                }

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                repeat(10) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        repeat(columns) {
                            LoadingArtistCard(
                                Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                            )
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.small3))
                }
            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingType,
                navigateBack = navigateBack
            )

            LoadingType.Content -> LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .nestedScroll(scroll.nestedScrollConnection)
                    .padding(it),
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {
                item(span = { GridItemSpan((maxLineSpan)) }) {
                    AddArtistSearchFilterChips(
                        Modifier.fillMaxWidth(),
                        state.searchFilterType,
                        onAction
                    )
                }

                item(span = { GridItemSpan((maxLineSpan)) }) {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                items(artist.itemCount) { index ->
                    artist[index]?.let { item ->
                        AddArtistCard(
                            modifier = Modifier.fillMaxWidth(),
                            item = item,
                            haptic = haptic,
                            isEditEnabled = state.isEditEnabled,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun AddArtistActions(
    isSaving: Boolean,
    isEditEnabled: Boolean,
    onAction: (AddArtistUiAction.OnSaveClick) -> Unit,
    haptic: HapticFeedback,
) {
    AnimatedVisibility(
        visible = isEditEnabled,
        enter = fadeIn(tween(600)) + expandHorizontally(tween(600)),
        exit = fadeOut(tween(600)) + shrinkHorizontally(tween(600)),
    ) {
        Card(
            modifier = Modifier
                .padding(MaterialTheme.dimens.small1)
                .size(40.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp,
                pressedElevation = 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ),
            onClick = {
                onAction(AddArtistUiAction.OnSaveClick)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = isEditEnabled && isSaving
                ) { state ->
                    when (state) {
                        true -> CircularProgressIndicator(
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.small1)
                                .size(40.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            strokeCap = StrokeCap.Round
                        )

                        false -> Icon(
                            imageVector = CheckIcon,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(.8f)
                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddArtistCompactScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                columns = 4,
                state = AddArtistUiState(
                    loadingType = LoadingType.Content,
                    selectedArtist = (1..5).map {
                        UiArtist(name = "That Cool Artist")
                    },
                    isSaving = true,
                    isSelectedBottomSheetOpen = false
                ),
                artist = flowOf(
                    PagingData.from(
                        (1..40).map {
                            UiArtist(
                                name = "That Cool Artist",
                                isSelected = Random.nextBoolean()
                            )
                        }
                    )
                ).collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}