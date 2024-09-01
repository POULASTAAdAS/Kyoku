package com.poulastaa.play.presentation.add_new_album

import com.poulastaa.core.domain.model.PagingAlbumData

fun PagingAlbumData.toUiAlbum() = AddAlbumUiAlbum(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    artist = this.artist,
    releaseYear = this.releaseYear
)