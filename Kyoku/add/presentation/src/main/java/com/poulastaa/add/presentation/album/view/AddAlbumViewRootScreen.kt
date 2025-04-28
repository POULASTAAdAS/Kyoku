package com.poulastaa.add.presentation.album.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.add.presentation.album.UiAlbum
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiDetailedPrevSong
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.core.presentation.ui.components.AppErrorScreen

@Composable
internal fun AddAlbumViewRootScreen(
    album: UiAlbum,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    val viewmodel = hiltViewModel<AddAlbumViewViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    LaunchedEffect(album) {
        viewmodel.init(album)
    }

    ObserveAsEvent(viewmodel.uiEvent) {
        when (it) {
            is AddAlbumViewUiEvent.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    AddAlbumViewScreen(
        state = state,
        navigateBack = navigateBack
    )
}

@Composable
private fun AddAlbumViewScreen(
    state: AddAlbumViewUiState,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBasicTopBar(
                title = state.album.name,
                navigationIcon = ArrowDownIcon,
                navigateBack = navigateBack
            )
        }
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
                repeat(10) {
                    LoadingSongCard(
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        false
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
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

            LoadingType.Content -> LazyColumn(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {

            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddAlbumViewScreen(
                state = AddAlbumViewUiState(
                    loadingType = LoadingType.Content,
                    songs = (1..10).map {
                        UiDetailedPrevSong(
                            title = "That Cool Song",
                            artists = "That Cool Artist",
                        )
                    }
                )
            ) { }
        }
    }
}