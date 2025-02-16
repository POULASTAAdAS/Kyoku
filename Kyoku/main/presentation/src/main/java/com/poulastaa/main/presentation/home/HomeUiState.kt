package com.poulastaa.main.presentation.home

import androidx.compose.runtime.Stable
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.model.UiPrevSong

@Stable
internal data class HomeUiState(
    val savedItems: List<UiSavedItem> = emptyList(),
    val staticData: UiHomeData = UiHomeData(),
) {
    val canShowUi = staticData.popularAlbum.isNotEmpty()
            && staticData.popularArtistSong.isNotEmpty()
            && staticData.suggestedArtist.isNotEmpty()
}


@Stable
internal data class UiSavedItem(
    val id: PlaylistId = -1,
    val name: String = "",
    val posters: List<String> = emptyList(),
    val type: UiSaveItemType? = null,
)

internal enum class UiSaveItemType {
    PLAYLIST,
    ALBUM,
    ARTIST
}

@Stable
internal data class UiHomeData(
    val popularSongMix: List<UiPrevSong> = emptyList(),
    val popularSongFromYourTime: List<UiPrevSong> = emptyList(),
    val favouriteArtistMix: List<UiPrevSong> = emptyList(),
    val dayTypeSong: List<UiPrevSong> = emptyList(),

    val popularAlbum: List<UiPrevAlbum> = emptyList(),
    val suggestedArtist: List<UiPrevArtist> = emptyList(),
    val popularArtistSong: List<UiArtistWithSong> = emptyList(),
)

internal data class UiPrevAlbum(
    val id: AlbumId = -1,
    val name: String = "",
    val poster: String? = null,
)

internal data class UiArtistWithSong(
    val artist: UiPrevArtist = UiPrevArtist(),
    val songs: List<UiPrevSong> = emptyList(),
)