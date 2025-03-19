package com.poulastaa.view.presentation.artist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.view.presentation.artist.components.ViewArtistTopBar
import com.poulastaa.view.presentation.components.ViewScreenWrapperMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewArtistMediumScreen(
    scroll: TopAppBarScrollBehavior,
    state: ViewArtistUiState,
    onAction: (ViewArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ViewScreenWrapperMedium(
        scroll = scroll,
        data = state.mostPopularSongs,
        name = state.artist.name,
        cover = state.artist.cover,
        totalSongs = state.mostPopularSongs.size,
        loadingType = state.loadingType,
        isTypeArtist = true,
        isNotAlbum = true,
        onExplore = { onAction(ViewArtistUiAction.OnExploreArtist) },
        play = { onAction(ViewArtistUiAction.OnPlayAll(it)) },
        onSongClick = { type, songId ->
            onAction(ViewArtistUiAction.OnSongClick(type, songId))
        },
        onSongThreeDotClick = { songId ->
            onAction(ViewArtistUiAction.OnSongOptionsToggle(songId))
        },
        topBar = {
            ViewArtistTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ThemModeChanger.getGradiantBackground().first(),
                                Color.Transparent,
                            )
                        )
                    )
                    .padding(end = MaterialTheme.dimens.medium1)
                    .padding(top = MaterialTheme.dimens.medium2),
                scrollBehavior = scroll,
                artist = state.artist,
                onAction = onAction,
                navigateBack = navigateBack
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewArtistMediumScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = PREV_ARTIST_DATA,
                onAction = {},
                navigateBack = {}
            )
        }
    }
}