package com.poulastaa.play.presentation.root_drawer.library.model

import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist

data class LibraryUiData(
    val artist: List<UiArtist> = emptyList(),
    val playlist: List<UiPrevPlaylist> = emptyList(),
    val isFavouriteEntry: Boolean = false,
)
