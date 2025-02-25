package com.poulastaa.main.presentation.home.mapper

import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.toUiPrevSong
import com.poulastaa.main.domain.model.PayloadHomeData
import com.poulastaa.main.domain.model.PayloadSaveItemType
import com.poulastaa.main.domain.model.PayloadSavedItem
import com.poulastaa.main.presentation.components.UiSaveItemType
import com.poulastaa.main.presentation.components.UiSavedItem
import com.poulastaa.main.presentation.home.HomeUiState
import com.poulastaa.main.presentation.home.UiArtistWithSong
import com.poulastaa.main.presentation.home.UiHomeData
import com.poulastaa.main.presentation.home.UiPrevAlbum

internal fun DtoPrevArtist.toUiPrevArtist() = UiPrevArtist(
    id = this.id,
    name = this.name,
    cover = this.cover
)

internal fun PayloadSavedItem.toUiSavedItem() = UiSavedItem(
    id = this.id,
    name = this.name,
    posters = this.posters,
    type = when (this.type) {
        PayloadSaveItemType.PLAYLIST -> UiSaveItemType.PLAYLIST
        PayloadSaveItemType.ALBUM -> UiSaveItemType.ALBUM
        PayloadSaveItemType.ARTIST -> UiSaveItemType.ARTIST
    }
)

internal fun PayloadHomeData.toUiHomeState(spotlightItem: PayloadSavedItem) = HomeUiState(
    spotlightItem = spotlightItem.toUiSavedItem(),
    savedItems = this.savedItems.map { it.toUiSavedItem() }.filterNot { it.id == spotlightItem.id },
    staticData = UiHomeData(
        popularSongMix = this.staticData.popularSongMix.map { it.toUiPrevSong() },
        popularSongFromYourTime = this.staticData.popularSongFromYourTime.map { it.toUiPrevSong() },
        favouriteArtistMix = this.staticData.favouriteArtistMix.map { it.toUiPrevSong() },
        dayTypeSong = this.staticData.dayTypeSong.map { it.toUiPrevSong() },
        popularAlbum = this.staticData.popularAlbum.map {
            UiPrevAlbum(
                id = it.id,
                name = it.name,
                poster = it.poster
            )
        },
        suggestedArtist = this.staticData.suggestedArtist.map { it.toUiPrevArtist() },
        popularArtistSong = this.staticData.popularArtistSong.map {
            UiArtistWithSong(
                artist = it.artist.toUiPrevArtist(),
                songs = it.prevSong.map { it.toUiPrevSong() }
            )
        }
    )
)