package com.poulastaa.main.presentation.home

import androidx.compose.runtime.Stable
import com.poulastaa.core.presentation.designsystem.model.UiPreSong
import com.poulastaa.core.presentation.designsystem.model.UiPrevAlbum
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.main.presentation.components.UiSavedItem

@Stable
internal data class HomeUiState(
    val spotlightItem: UiSavedItem? = null,
    val savedItems: List<UiSavedItem> = emptyList(),
    val staticData: UiHomeData = UiHomeData(),
) {
    val canShowUi = staticData.popularAlbum.isNotEmpty()
            && staticData.popularArtistSong.isNotEmpty()
            && staticData.suggestedArtist.isNotEmpty()
}

@Stable
internal data class UiHomeData(
    val popularSongMix: List<UiPreSong> = emptyList(),
    val popularSongFromYourTime: List<UiPreSong> = emptyList(),
    val favouriteArtistMix: List<UiPreSong> = emptyList(),
    val dayTypeSong: List<UiPreSong> = emptyList(),

    val popularAlbum: List<UiPrevAlbum> = emptyList(),
    val suggestedArtist: List<UiPrevArtist> = emptyList(),
    val popularArtistSong: List<UiArtistWithSong> = emptyList(),
)


internal data class UiArtistWithSong(
    val artist: UiPrevArtist = UiPrevArtist(),
    val songs: List<UiPreSong> = emptyList(),
)

internal enum class UiHomeExploreType {
    POPULAR_SONG_MIX,
    POPULAR_YEAR_MIX,
    SAVED_ARTIST_SONG_MIX,
    DAY_TYPE_MIX
}