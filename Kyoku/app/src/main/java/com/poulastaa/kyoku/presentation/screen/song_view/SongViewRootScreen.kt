package com.poulastaa.kyoku.presentation.screen.song_view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.presentation.screen.song_view.common.SongViewErrScreen
import com.poulastaa.kyoku.presentation.screen.song_view.playlist.PlaylistContentLoading

@Composable
fun SongViewRootScreen(
    viewModel: SongViewViewModel = hiltViewModel(),
    type: String,
    id: Long,
    name: String,
    navigate: (UiEvent.Navigate) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData(type, id, name)
    }

    when (viewModel.state.type) {
        ItemsType.PLAYLIST -> {
            if (viewModel.state.isLoading) {
                PlaylistContentLoading()
            } else {

            }
        }

        ItemsType.ALBUM -> {

        }

        ItemsType.ALBUM_PREV -> {

        }

        ItemsType.ARTIST -> {

        }

        ItemsType.ARTIST_MIX -> {

        }

        ItemsType.ARTIST_MORE -> {

        }

        ItemsType.FAVOURITE -> {

        }

        ItemsType.SONG -> {

        }

        ItemsType.HISTORY -> {

        }

        ItemsType.ERR -> {
            SongViewErrScreen()
        }
    }
}