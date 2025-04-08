package com.poulastaa.add.presentation.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingTopBar
import com.poulastaa.add.presentation.playlist.components.LoadingNavigationBall
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistCompactScreen(
    state: AddSongToPlaylistUiState,
    searchData: LazyPagingItems<AddToPlaylistUiItem>,
    onAction: (AddSongToPlaylistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            when (state.loadingType) {
                is LoadingType.Loading -> AddSongToPlaylistLoadingTopBar(navigateBack)

                is LoadingType.Content -> {

                }

                else -> Unit
            }
        }
    ) {
        when (state.loadingType) {
            LoadingType.Loading -> Column(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.95f)
                        .verticalScroll(rememberScrollState()),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    repeat(10) {
                        LoadingSongCard()
                        Spacer(Modifier.height(MaterialTheme.dimens.small1))
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LoadingNavigationBall(MaterialTheme.colorScheme.background)
                    Spacer(Modifier.width(MaterialTheme.dimens.small2))

                    repeat(3) {
                        LoadingNavigationBall()
                        Spacer(Modifier.width(MaterialTheme.dimens.small2))
                    }
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

            LoadingType.Content -> {

            }
        }
    }
}


internal val PREV_DATA = flowOf(
    PagingData.from(
        ((1..5).map {
            AddToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Song",
                type = AddToPlaylistItemUiType.SONG
            )
        } + (1..5).map {
            AddToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Artist",
                type = AddToPlaylistItemUiType.ARTIST
            )
        } + (1..5).map {
            AddToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Playlist",
                type = AddToPlaylistItemUiType.PLAYLIST
            )
        } + (1..5).map {
            AddToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Artist",
                type = AddToPlaylistItemUiType.ALBUM
            )
        }).shuffled()
    )
)

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddSongToPlaylistCompactScreen(
                state = AddSongToPlaylistUiState(
                    loadingType = LoadingType.Content
                ),
                searchData = PREV_DATA.collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}