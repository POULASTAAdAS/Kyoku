package com.poulastaa.view.presentation.others

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.view.presentation.components.ViewScreenWrapperMedium
import com.poulastaa.view.presentation.model.UiViewType
import com.poulastaa.view.presentation.others.compoenets.ViewOtherTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewOtherMediumScreen(
    scroll: TopAppBarScrollBehavior,
    state: ViewOtherUiState,
    onAction: (ViewOtherUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ViewScreenWrapperMedium(
        scroll = scroll,
        data = state.listOfSongs,
        name = state.root.title,
        cover = state.root.poster,
        totalSongs = state.listOfSongs.size,
        loadingType = state.loadingType,
        isTypeArtist = false,
        isNotAlbum = UiViewType.ALBUM != state.type,
        play = {
            onAction(ViewOtherUiAction.OnPlayAll(it))
        },
        onSongClick = { type, songId ->
            onAction(ViewOtherUiAction.OnSongClick(type, songId))
        },
        onSongThreeDotClick = {
            onAction(ViewOtherUiAction.OnSongMenuToggle(it))
        },
        topBar = {
            ViewOtherTopBar(
                scroll = scroll,
                title = state.root.title,
                type = state.type,
                navigateBack = navigateBack,
                onMenuClick = {
                    onAction(ViewOtherUiAction.OnMenuClick)
                }
            )
        },
        navigateBack = navigateBack
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
            ViewOtherMediumScreen(
                state = PREV_VIEW_DATA,
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}