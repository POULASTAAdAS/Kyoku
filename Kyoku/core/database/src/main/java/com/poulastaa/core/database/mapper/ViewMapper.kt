package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.database.entity.SongEntity
import com.poulastaa.core.database.model.PlaylistResult
import com.poulastaa.core.domain.model.AlbumData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.ViewData

fun List<PlaylistResult>.toViewData(id: Long) = ViewData(
    id = id,
    name = this.first().playlistName,
    listOfSong = this.map { it.toPlaylistSong() }
)

fun SongEntity.toPlaylistSong() = PlaylistSong(
    id = this.id,
    coverImage = this.coverImage,
    title = this.title,
    artist = this.artistName
)

fun AlbumData.toAlbumEntity() = AlbumEntity(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage
)