package com.poulastaa.play.presentation.root_drawer

import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.model.PrevAlbum
import com.poulastaa.core.domain.model.PrevSavedPlaylist
import com.poulastaa.core.domain.model.SongOtherData
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.presentation.player.MorePlayerInfo
import com.poulastaa.play.presentation.player.OtherUiInfo
import com.poulastaa.play.presentation.player.PlayerUiInfo
import com.poulastaa.play.presentation.player.PlayerUiSong
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.play.presentation.view.components.ViewDataType

fun String.toDrawerScreen() = when (this) {
    SaveScreen.HOME.name -> DrawerScreen.Home
    else -> DrawerScreen.Library
}

fun String.toSaveScreen() = when (this) {
    SaveScreen.HOME.name -> SaveScreen.HOME
    else -> SaveScreen.LIBRARY
}


fun String.toDrawScreenRoute() = when (this) {
    SaveScreen.HOME.name -> DrawerScreen.Home.route
    else -> DrawerScreen.Library.route
}

fun PrevSavedPlaylist.toUiPrevPlaylist() = UiPrevPlaylist(
    id = this.id,
    name = this.name,
    urls = this.coverImageList
)

fun PrevAlbum.toUiAlbum() = UiPrevAlbum(
    id = this.albumId,
    name = this.name,
    coverImage = this.coverImage
)

fun ViewDataType.toPlayType() = when (this) {
    ViewDataType.PLAYLIST -> PlayType.PLAYLIST
    ViewDataType.ALBUM -> PlayType.ALBUM
    ViewDataType.FEV -> PlayType.FEV
    ViewDataType.ARTIST_MIX -> PlayType.ARTIST_MIX
    ViewDataType.POPULAR_MIX -> PlayType.POPULAR_MIX
    ViewDataType.OLD_MIX -> PlayType.OLD_MIX
}

fun PlayerSong.toPlayerUiSong() = PlayerUiSong(
    index = this.id,
    songId = this.songId,
    title = this.title,
    artist = this.artist,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    releaseYear = this.releaseYear,
)

fun PlayerInfo.toPlayerUiInfo() = PlayerUiInfo(
    isShuffledEnabled = this.isShuffledEnabled,
    repeatState = this.repeatState,
    isPlaying = this.isPlaying,
    hasNext = this.hasNext,
    hasPrev = this.hasPrev,
    currentPlayingIndex = 0,
    other = OtherUiInfo(
        otherId = this.otherId,
        playType = this.type
    )
)

fun SongOtherData.toMorePlayerInfo() = MorePlayerInfo(
    id = this.otherId,
    title = this.title,
    coverImage = this.coverImage,
    isPlaylist = this.isPlaylist
)
