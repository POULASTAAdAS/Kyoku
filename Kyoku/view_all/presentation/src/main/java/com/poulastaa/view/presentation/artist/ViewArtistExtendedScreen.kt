package com.poulastaa.view.presentation.artist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.view.presentation.artist.components.ViewArtistTopBar
import com.poulastaa.view.presentation.components.ViewScreenWrapperExtended

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewArtistExtendedScreen(
    state: ViewArtistUiState,
    onAction: (ViewArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ViewScreenWrapperExtended(
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
                    .padding(top = MaterialTheme.dimens.medium2),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                artist = state.artist,
                onAction = onAction,
                navigateBack = navigateBack
            )
        }
    )
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
            ViewArtistExtendedScreen(
                state = PREV_ARTIST_DATA,
                onAction = {},
                navigateBack = {}
            )
        }
    }
}