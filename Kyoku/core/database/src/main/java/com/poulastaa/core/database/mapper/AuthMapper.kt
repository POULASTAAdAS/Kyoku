package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PrevAlbum

fun PlaylistData.toPlaylistEntity() = PlaylistEntity(
    id = this.id,
    name = this.name,
    points = 0,
)

fun PrevAlbum.toAlbumEntity() = AlbumEntity(
    id = this.albumId,
    name = this.name,
    coverImage = this.coverImage
)