package com.poulastaa.play.presentation.view_artist

import com.poulastaa.core.domain.model.ViewArtistData
import com.poulastaa.core.domain.model.ViewArtistSong
import com.poulastaa.core.presentation.ui.model.ArtistUiSong
import com.poulastaa.play.presentation.root_drawer.library.toUiArtist

fun ViewArtistSong.toUiArtistSong() = ArtistUiSong(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    isExpanded = false,
    popularity = this.popularity
)

fun ViewArtistData.toUiArtistData(isFollowing: Boolean) = UiArtistData(
    isArtistFollowed = isFollowing,
    popularity = this.followers,
    artist = this.artist.toUiArtist(),
    listOfSong = this.listOfSong.map { it.toUiArtistSong() }
)
