package com.poulastaa.kyoku.presentation.screen.song_view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.presentation.screen.song_view.album.AlbumScreen
import com.poulastaa.kyoku.presentation.screen.song_view.artist.ArtistScreen
import com.poulastaa.kyoku.presentation.screen.song_view.artist_mix.ArtistMixScreen
import com.poulastaa.kyoku.presentation.screen.song_view.common.SongViewContentLoading
import com.poulastaa.kyoku.presentation.screen.song_view.common.SongViewErrScreen
import com.poulastaa.kyoku.presentation.screen.song_view.daily_mix.DailyMixScreen
import com.poulastaa.kyoku.presentation.screen.song_view.favourites.FavouriteScreen
import com.poulastaa.kyoku.presentation.screen.song_view.playlist.PlaylistScreen

@Composable
fun SongViewRootScreen(
    viewModel: SongViewViewModel = hiltViewModel(),
    type: String,
    id: Long,
    name: String,
    isApiCall: Boolean,
    isSmallPhone: Boolean = LocalConfiguration.current.screenWidthDp <= 411,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    context: Context = LocalContext.current,
    navigateBack: () -> Unit,
    navigate: (UiEvent) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData(type, id, name, isApiCall)
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navigate.invoke(event)
                is UiEvent.NavigateWithData -> navigate.invoke(event)
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
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
            else ArtistScreen(
                data = viewModel.state.data.artist,
                isDarkThem = isDarkThem,
                isCookie = viewModel.state.isCooke,
                headerValue = viewModel.state.headerValue,
                isSmallPhone = isSmallPhone,
                navigateBack = navigateBack,
                viewAll = viewModel::onEvent
            )
        }

        ItemsType.ARTIST_MIX -> {
            if (viewModel.state.isLoading ||
                viewModel.state.data.dailyMixOrArtistMix.listOfSong.isEmpty()
            ) SongViewContentLoading(
                isFavourite = false,
                isSmallPhone = isSmallPhone
            )
            else ArtistMixScreen(
                artistMix = viewModel.state.data.dailyMixOrArtistMix,
                isDarkThem = isDarkThem,
                isCookie = viewModel.state.isCooke,
                headerValue = viewModel.state.headerValue,
                poster = viewModel.state.data.dailyMixOrArtistMix.listOfSong[0].coverImage,
                isSmallPhone = isSmallPhone,
                navigateBack = navigateBack
            )
        }

        ItemsType.DAILY_MIX -> {
            if (viewModel.state.isLoading ||
                viewModel.state.data.dailyMixOrArtistMix.listOfSong.isEmpty()
            ) SongViewContentLoading(
                isFavourite = false,
                isSmallPhone = isSmallPhone
            )
            else DailyMixScreen(
                dailyMix = viewModel.state.data.dailyMixOrArtistMix,
                isDarkThem = isDarkThem,
                isCookie = viewModel.state.isCooke,
                headerValue = viewModel.state.headerValue,
                poster = viewModel.state.data.dailyMixOrArtistMix.listOfSong[0].coverImage,
                isSmallPhone = isSmallPhone,
                navigateBack = navigateBack
            )
        }

        ItemsType.ARTIST_MORE -> {

        }

        ItemsType.FAVOURITE -> {
            if (viewModel.state.isLoading ||
                viewModel.state.data.favourites.listOfSong.isEmpty()
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