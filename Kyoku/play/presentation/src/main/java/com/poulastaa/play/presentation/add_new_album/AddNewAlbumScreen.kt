package com.poulastaa.play.presentation.add_new_album

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThreeDotIcon
import com.poulastaa.core.presentation.designsystem.components.CompactErrorScreen
import com.poulastaa.core.presentation.designsystem.components.DummySearch
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.CustomSnackBar
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.core.presentation.ui.imageReqSongCover
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.add_new_album.components.AddNewAlbumLoadingAnimation
import com.poulastaa.play.presentation.add_new_album.components.AddNewAlbumTopBar
import java.time.LocalDate
import kotlin.random.Random

@Composable
fun AddNewAlbumRootScreen(
    modifier: Modifier = Modifier,
    viewModel: AddNewAlbumViewModel = hiltViewModel(),
    navigate: (AddNewAlbumOtherScreen) -> Unit,
    navigateBack: () -> Unit
) {
    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is AddNewAlbumUiAction.Navigate -> navigate(event.screen)
        }
    }

    AddNewAlbumScreen(
        modifier = modifier,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = {
            if (viewModel.state.isSearchEnabled) viewModel.onEvent(AddAlbumUiEvent.OnSearchToggle)
            else if (viewModel.state.isMassSelectEnabled) viewModel.onEvent(AddAlbumUiEvent.OnMassSelectToggle)
            else navigateBack()
        }
    )

    BackHandler(
        enabled = viewModel.state.isSearchEnabled ||
                viewModel.state.isMassSelectEnabled,
        onBack = {
            if (viewModel.state.isSearchEnabled) viewModel.onEvent(AddAlbumUiEvent.OnSearchToggle)
            else viewModel.onEvent(AddAlbumUiEvent.OnMassSelectToggle)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNewAlbumScreen(
    modifier: Modifier = Modifier,
    state: AddAlbumUiState,
    onEvent: (AddAlbumUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = modifier,
        topBar = {
            AddNewAlbumTopBar(
                scroll = scroll,
                isSearch = state.isSearchEnabled,
                searchQuery = state.searchQuery,
                focusRequester = focusRequester,
                isMassSelectEnabled = state.isMassSelectEnabled,
                onSearchChange = {
                    onEvent(AddAlbumUiEvent.OnSearchQueryChange(it))
                },
                navigateBack = navigateBack
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { paddingValues ->
        AnimatedContent(state.loadingState, label = "add_new_album") { loadingState ->
            when (loadingState) {
                DataLoadingState.LOADING -> AddNewAlbumLoadingAnimation(paddingValues)
                DataLoadingState.ERROR -> CompactErrorScreen()

                DataLoadingState.LOADED -> {
                    Column {
                        CustomSnackBar(state.toast, paddingValues)

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(horizontal = MaterialTheme.dimens.medium1)
                        ) {
                            item {
                                AddNewAlbumDummySearch(
                                    isVisible = !state.isSearchEnabled &&
                                            !state.isMassSelectEnabled,
                                    onClick = { onEvent(AddAlbumUiEvent.OnSearchToggle) }
                                )
                            }

                            items(data) { album ->
                                AlbumCard(
                                    modifier = Modifier.clickable(
                                        onClick = {
                                            if (state.isMassSelectEnabled)
                                                onEvent(
                                                    AddAlbumUiEvent.OnCheckChange(
                                                        album.id,
                                                        !album.isSelected
                                                    )
                                                )
                                            else onEvent(AddAlbumUiEvent.OnAlbumClick(album.id))
                                        }
                                    ),
                                    header = state.header,
                                    list = state.threeDotOperations,
                                    isMassSelectEnabled = state.isMassSelectEnabled,
                                    album = album,
                                    onEvent = onEvent
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
private fun AddNewAlbumDummySearch(
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
                header = stringResource(R.string.search_album),
                onClick = onClick
            )

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}

@Composable
private fun AlbumCard(
    modifier: Modifier = Modifier,
    header: String,
    list: List<AddAlbumOperation>,
    album: AddAlbumUiAlbum,
    isMassSelectEnabled: Boolean,
    onEvent: (AddAlbumUiEvent.ThreeDotEvent) -> Unit
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.small3)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(isMassSelectEnabled) {
            Checkbox(
                checked = album.isSelected,
                onCheckedChange = {}
            )
        }

        Card(
            modifier = Modifier.aspectRatio(1f),
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = album.coverImage
                ),
                modifier = Modifier
                    .aspectRatio(1f),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = album.name,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = album.artist,
                    color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(.8f)
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = album.releaseYear,
                    color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                enabled = !isMassSelectEnabled,
                onClick = {
                    onEvent(AddAlbumUiEvent.ThreeDotEvent.OnClick(album.id))
                }
            ) {
                Icon(
                    imageVector = ThreeDotIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(.6f)
                )
            }

            DropdownMenu(
                expanded = album.isExtended,
                onDismissRequest = { onEvent(AddAlbumUiEvent.ThreeDotEvent.OnCloseClick(album.id)) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = MaterialTheme.dimens.small2)
            ) {
                list.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.value) },
                        onClick = {
                            onEvent(AddAlbumUiEvent.ThreeDotEvent.OnThreeDotItemClick(album.id, it))
                        }
                    )
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    var search by remember { mutableStateOf(false) }

    AppThem {
        AddNewAlbumScreen(
            state = AddAlbumUiState(
                isSearchEnabled = search,
                loadingState = DataLoadingState.LOADED,
                threeDotOperations = listOf(
                    AddAlbumOperation.PLAY,
                    AddAlbumOperation.SAVE_ALBUM,
                ),
                isMassSelectEnabled = false,
            ),
            onEvent = {
                if (it is AddAlbumUiEvent.OnSearchToggle) {
                    search = true
                }
            }
        ) {
            search = false
        }
    }
}

private val data = (1..10).map {
    AddAlbumUiAlbum(
        id = it.toLong(),
        name = "Album  $it",
        artist = "That cool artist $it",
        releaseYear = LocalDate.now().year.toString(),
        isExtended = false,
        isSelected = Random.nextBoolean()
    )
}