package com.poulastaa.play.presentation.add_new_artist

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.domain.model.ArtistPagingType
import com.poulastaa.core.presentation.designsystem.MoreFromArtistIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.components.AppFilterChip
import com.poulastaa.core.presentation.designsystem.components.DummySearch
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.CustomSnackBar
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.add_new_artist.components.AddNewArtistArtistCard
import com.poulastaa.play.presentation.add_new_artist.components.AddNewArtistLoadingAnimation
import com.poulastaa.play.presentation.add_new_artist.components.AddNewArtistTopBar

@Composable
fun AddNewArtistRootScreen(
    modifier: Modifier = Modifier,
    viewModel: AddNewArtistVewModel = hiltViewModel(),
    navigate: (AddNewArtistOtherScreen) -> Unit,
    navigateBack: () -> Unit
) {
    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is AddNewArtistUiAction.Navigate -> navigate(event.screen)
        }
    }

    AddNewArtistScreen(
        modifier = modifier,
        state = viewModel.state,
        artist = viewModel.artist.collectAsLazyPagingItems(),
        onEvent = viewModel::onEvent,
        navigateBack = {
            if (viewModel.state.isSearchEnabled) viewModel.onEvent(AddArtistUiEvent.OnSearchToggle)
            else if (viewModel.state.isMassSelectEnabled) viewModel.onEvent(AddArtistUiEvent.OnMassSelectToggle)
            else navigateBack()
        }
    )

    if (viewModel.state.isSearchEnabled ||
        viewModel.state.isMassSelectEnabled
    ) BackHandler(
        onBack = {
            if (viewModel.state.isSearchEnabled) viewModel.onEvent(AddArtistUiEvent.OnSearchToggle)
            else viewModel.onEvent(AddArtistUiEvent.OnMassSelectToggle)
        }
    ) else BackHandler {
        navigateBack()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun AddNewArtistScreen(
    modifier: Modifier = Modifier,
    state: AddNewArtistUiState,
    onEvent: (AddArtistUiEvent) -> Unit,
    artist: LazyPagingItems<AddArtistUiArtist>,
    navigateBack: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = modifier,
        topBar = {
            AddNewArtistTopBar(
                scroll = scroll,
                isSearch = state.isSearchEnabled,
                searchQuery = state.searchQuery,
                focusRequester = focusRequester,
                isMassSelectEnabled = state.isMassSelectEnabled,
                isMakingApiCall = state.isMakingApiCall,
                onSearchChange = {
                    onEvent(AddArtistUiEvent.OnSearchQueryChange(it))
                },
                onSaveClick = {
                    onEvent(AddArtistUiEvent.OnSaveClick)
                },
                navigateBack = navigateBack
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = artist.itemCount == 0,
            label = "add_new_artist"
        ) { loadingState ->
            when (loadingState) {
                true -> AddNewArtistLoadingAnimation(paddingValues)

                false -> Column {
                    CustomSnackBar(state.toast, paddingValues)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(count = 3),
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .nestedScroll(scroll.nestedScrollConnection)
                            .then(
                                if (state.toast.isVisible) Modifier
                                    .padding(
                                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                                        bottom = paddingValues.calculateBottomPadding(),
                                        end = paddingValues.calculateStartPadding(LayoutDirection.Rtl)
                                    )
                                else Modifier.padding(paddingValues)
                            )
                            .padding(horizontal = MaterialTheme.dimens.medium1),
                    ) {
                        item(
                            span = {
                                GridItemSpan(maxLineSpan)
                            }
                        ) {
                            AddNewArtistDummySearch(
                                isVisible = !state.isSearchEnabled &&
                                        !state.isMassSelectEnabled,
                                onClick = { onEvent(AddArtistUiEvent.OnSearchToggle) }
                            )
                        }

                        item(
                            span = {
                                GridItemSpan(maxLineSpan)
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = MaterialTheme.dimens.medium1),
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                            ) {
                                AppFilterChip(
                                    text = stringResource(R.string.all),
                                    icon = UserIcon,
                                    selected = state.type == ArtistPagingType.ALL,
                                    onClick = {
                                        onEvent(
                                            AddArtistUiEvent.OnFilterTypeChange(
                                                ArtistPagingType.ALL
                                            )
                                        )

                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                )

                                AppFilterChip(
                                    text = stringResource(R.string.international),
                                    icon = MoreFromArtistIcon,
                                    selected = state.type == ArtistPagingType.INTERNATIONAL,
                                    onClick = {
                                        onEvent(
                                            AddArtistUiEvent.OnFilterTypeChange(
                                                ArtistPagingType.INTERNATIONAL
                                            )
                                        )

                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                )
                            }
                        }

                        items(artist.itemCount) { index ->
                            artist[index]?.let { artist ->
                                AddNewArtistArtistCard(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .padding(MaterialTheme.dimens.small2)
                                        .combinedClickable(
                                            onClick = {
                                                if (state.isMassSelectEnabled) onEvent(
                                                    AddArtistUiEvent.OnCheckChange(
                                                        artist.id,
                                                        !artist.isSelected
                                                    )
                                                )
                                                else {
                                                    if (state.isSearchEnabled) onEvent(
                                                        AddArtistUiEvent.OnSearchToggle
                                                    )
                                                    onEvent(AddArtistUiEvent.OnArtistClick(artist.id))
                                                }
                                            },
                                            onLongClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                                onEvent(AddArtistUiEvent.OnMassSelectToggle)
                                                onEvent(
                                                    AddArtistUiEvent.OnCheckChange(
                                                        artist.id,
                                                        status = true
                                                    )
                                                )
                                            }
                                        ),
                                    header = state.header,
                                    artist = artist
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddNewArtistDummySearch(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 300
            )
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = 300
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 300
            )
        ) + slideOutVertically(
            animationSpec = tween(
                durationMillis = 300
            )
        )
    ) {
        Column {
            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            DummySearch(
                header = stringResource(R.string.search_artist),
                onClick = onClick
            )
        }
    }
}