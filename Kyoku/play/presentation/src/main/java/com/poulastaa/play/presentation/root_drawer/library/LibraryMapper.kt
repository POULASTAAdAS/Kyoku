package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.PinnedData
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.play.presentation.root_drawer.library.model.PinnedUiData

fun Artist.toUiArtist() = UiArtist(
    id = id,
    name = name,
    coverImageUrl = coverImage ?: ""
)

fun PinnedData.toPinnedUiData() = PinnedUiData(
    id = this.id,
    name = this.name,
    urls = this.urls,
    pinnedType = this.pinnedType
)

fun PinnedUiData.toUiAlbum() = UiPrevAlbum(
    id = this.id,
    name = this.name,
    coverImage = this.urls.firstOrNull() ?: ""
)

fun PinnedUiData.toUiArtist() = UiArtist(
    id = this.id,
    name = this.name,
    coverImageUrl = this.urls.firstOrNull() ?: ""
)