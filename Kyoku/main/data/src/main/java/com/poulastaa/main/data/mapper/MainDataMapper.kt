package com.poulastaa.main.data.mapper

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoRelationSongAlbum
import com.poulastaa.core.domain.model.DtoRelationSongPlaylist
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId

@JvmName("toDtoRelationSongPlaylist")
internal fun List<Pair<PlaylistId, List<SongId>>>.toDtoRelationSongPlaylist() =
    this.map { (playlistId, list) ->
        DtoRelationSongPlaylist(
            playlistId = playlistId,
            list = list
        )
    }

@JvmName("toDtoRelationSongAlbum")
internal fun List<Pair<SongId, List<SongId>>>.toDtoRelationSongAlbum() =
    this.map { (albumId, list) ->
        DtoRelationSongAlbum(
            albumId = albumId,
            list = list
        )
    }

internal fun DtoAlbum.toDtoPrevAlbum() = DtoPrevAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster,
)

internal fun List<Pair<ArtistId, List<SongId>>>.toDtoSuggestedArtistSong() =
    this.map { (artistId, list) ->
        DtoRelationSuggestedArtistSong(
            artistId = artistId,
            list = list
        )
    }