package com.poulastaa.main.presentation.home.mapper

import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.toUiPrevSong
import com.poulastaa.main.domain.model.MainAllowedNavigationScreens
import com.poulastaa.main.domain.model.PayloadHomeData
import com.poulastaa.main.domain.model.PayloadSaveItemType
import com.poulastaa.main.domain.model.PayloadSavedItem
import com.poulastaa.main.presentation.components.UiMainViewMoreItemType
import com.poulastaa.main.presentation.components.UiSaveItemType
import com.poulastaa.main.presentation.components.UiSavedItem
import com.poulastaa.main.presentation.home.HomeUiEvent
import com.poulastaa.main.presentation.home.HomeUiState
import com.poulastaa.main.presentation.home.UiArtistWithSong
import com.poulastaa.main.presentation.home.UiHomeData
import com.poulastaa.main.presentation.home.UiHomeExploreType
import com.poulastaa.main.presentation.home.UiPrevAlbum
import com.poulastaa.main.presentation.library.LibraryUiEvent

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

internal fun HomeUiEvent.NavigateToView.toMainAllowedNavigationScreensHome() =
    MainAllowedNavigationScreens.NavigateToView(
        type = this.type,
        otherId = this.otherId
    )

internal fun LibraryUiEvent.NavigateToView.toMainAllowedNavigationScreensLibrary() =
    MainAllowedNavigationScreens.NavigateToView(
        type = this.type,
        otherId = this.otherId
    )

internal fun UiSaveItemType.toNavigateToViewHome(id: Long) = when (this) {
    UiSaveItemType.PLAYLIST -> HomeUiEvent.NavigateToView(
        type = ViewType.PLAYLIST,
        otherId = id
    )

    UiSaveItemType.ALBUM -> HomeUiEvent.NavigateToView(
        type = ViewType.ALBUM,
        otherId = id
    )

    UiSaveItemType.ARTIST -> HomeUiEvent.NavigateToView(
        type = ViewType.ARTIST,
        otherId = id
    )
}

internal fun UiSaveItemType.toNavigateToViewLibrary(id: Long) = when (this) {
    UiSaveItemType.PLAYLIST -> LibraryUiEvent.NavigateToView(
        type = ViewType.PLAYLIST,
        otherId = id
    )

    UiSaveItemType.ALBUM -> LibraryUiEvent.NavigateToView(
        type = ViewType.ALBUM,
        otherId = id
    )

    UiSaveItemType.ARTIST -> LibraryUiEvent.NavigateToView(
        type = ViewType.ARTIST,
        otherId = id
    )
}

internal fun UiHomeExploreType.toNavigateToView() = HomeUiEvent.NavigateToView(
    type = when (this) {
        UiHomeExploreType.POPULAR_SONG_MIX -> ViewType.POPULAR_SONG_MIX
        UiHomeExploreType.POPULAR_YEAR_MIX -> ViewType.POPULAR_YEAR_MIX
        UiHomeExploreType.SAVED_ARTIST_SONG_MIX -> ViewType.SAVED_ARTIST_SONG_MIX
        UiHomeExploreType.DAY_TYPE_MIX -> ViewType.DAY_TYPE_MIX
    },
    otherId = -1
)

internal fun UiMainViewMoreItemType.toNavigateToView(id: Long) = HomeUiEvent.NavigateToView(
    type = when (this) {
        UiMainViewMoreItemType.EXPLORE_ALBUM -> ViewType.ALBUM
        UiMainViewMoreItemType.EXPLORE_ARTIST -> ViewType.ARTIST
        else -> throw IllegalArgumentException("Invalid view type $this")
    },
    otherId = id
)
