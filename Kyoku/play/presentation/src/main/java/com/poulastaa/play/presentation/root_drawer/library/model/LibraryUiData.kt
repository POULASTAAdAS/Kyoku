package com.poulastaa.play.presentation.root_drawer.library.model

import com.poulastaa.core.domain.model.PinnedType
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum

data class LibraryUiData(
    val playlist: List<UiPrevPlaylist> = emptyList(),
    val album: List<UiPrevAlbum> = emptyList(),
    val artist: List<UiArtist> = emptyList(),
    val isFavouriteEntry: Boolean = false,

    val pinned: List<PinnedUiData> = emptyList()
)


data class PinnedUiData(
    val id: Long,
    val name: String,
    val urls: List<String>,
    val pinnedType: PinnedType
)