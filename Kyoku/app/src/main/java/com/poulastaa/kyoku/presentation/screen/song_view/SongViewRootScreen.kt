package com.poulastaa.kyoku.presentation.screen.song_view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.presentation.screen.song_view.album.AlbumScreen
import com.poulastaa.kyoku.presentation.screen.song_view.common.SongViewContentLoading
import com.poulastaa.kyoku.presentation.screen.song_view.common.SongViewErrScreen
import com.poulastaa.kyoku.presentation.screen.song_view.favourites.FavouriteScreen
import com.poulastaa.kyoku.presentation.screen.song_view.playlist.PlaylistScreen

@Composable
fun SongViewRootScreen(
    viewModel: SongViewViewModel = hiltViewModel(),
    type: String,
    id: Long,
    name: String,
    isSmallPhone: Boolean = LocalConfiguration.current.screenWidthDp <= 411,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    navigateBack: () -> Unit,
    navigate: (UiEvent.Navigate) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData(type, id, name)
    }

    when (viewModel.state.type) {
        ItemsType.PLAYLIST -> {
            if (viewModel.state.isLoading ||
                viewModel.state.data.playlist.listOfSong.isEmpty()
            ) {
                SongViewContentLoading(isSmallPhone = isSmallPhone)
            } else {
                PlaylistScreen(
                    data = viewModel.state.data.playlist,
                    isDarkThem = isDarkThem,
                    isCookie = viewModel.state.isCooke,
                    headerValue = viewModel.state.headerValue,
                    poster = viewModel.state.data.playlist.listOfSong[0].coverImage,
                    isSmallPhone = isSmallPhone,
                    navigateBack = navigateBack
                )
            }
        }

        ItemsType.ALBUM -> {
            if (viewModel.state.isLoading ||
                viewModel.state.data.album.listOfSong.isEmpty()
            ) SongViewContentLoading(isSmallPhone = isSmallPhone)
            else AlbumScreen(
                album = viewModel.state.data.album,
                isDarkThem = isDarkThem,
                isCookie = viewModel.state.isCooke,
                headerValue = viewModel.state.headerValue,
                poster = viewModel.state.data.album.listOfSong[0].coverImage,
                isSmallPhone = isSmallPhone,
                navigateBack = navigateBack
            )
        }

        ItemsType.ALBUM_PREV -> {

        }

        ItemsType.ARTIST -> {
            if (viewModel.state.isLoading ||
                viewModel.state.data.artist.listOfSong.isEmpty()
            ) SongViewContentLoading(isSmallPhone = isSmallPhone)
            else {
                
            }
        }

        ItemsType.ARTIST_MIX -> {

        }

        ItemsType.ARTIST_MORE -> {

        }

        ItemsType.FAVOURITE -> {
            if (viewModel.state.isLoading ||
                viewModel.state.data.favourites.isEmpty()
            ) SongViewContentLoading(
                isFavourite = true,
                isSmallPhone = isSmallPhone
            )
            else FavouriteScreen(
                data = viewModel.state.data.favourites,
                isDarkThem = isDarkThem,
                isCookie = viewModel.state.isCooke,
                headerValue = viewModel.state.headerValue,
                isSmallPhone = isSmallPhone,
                navigateBack = navigateBack
            )
        }

        ItemsType.SONG -> {

        }

        ItemsType.HISTORY -> {

        }

        ItemsType.ERR -> {
            SongViewErrScreen {
                navigateBack.invoke()
            }
        }
    }
}