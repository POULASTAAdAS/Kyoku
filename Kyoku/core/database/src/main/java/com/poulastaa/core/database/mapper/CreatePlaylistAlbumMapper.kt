package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.domain.model.PrevAlbum

fun AlbumEntity.toPrevAlbum() = PrevAlbum(
    albumId = this.id,
    name = this.name,
    coverImage = this.coverImage
)