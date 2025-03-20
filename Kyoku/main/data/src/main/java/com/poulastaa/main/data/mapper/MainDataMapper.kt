package com.poulastaa.main.data.mapper

import androidx.work.ListenableWorker
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import com.poulastaa.core.domain.model.DtoRelationSongAlbum
import com.poulastaa.core.domain.model.DtoRelationSongPlaylist
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.main.domain.model.PayloadSaveItemType
import com.poulastaa.main.domain.model.PayloadSavedItem

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

internal fun DtoPrevArtist.toPayloadItem() = PayloadSavedItem(
    id = this.id,
    name = this.name,
    posters = listOf(this.cover ?: ""),
    type = PayloadSaveItemType.ARTIST
)

internal fun DtoPrevPlaylist.toPayloadItem() = PayloadSavedItem(
    id = this.id,
    name = this.title,
    posters = this.posters.map { it ?: "" },
    type = PayloadSaveItemType.PLAYLIST
)

internal fun DtoPrevAlbum.toPayloadItem() = PayloadSavedItem(
    id = this.id,
    name = this.name,
    posters = listOf(this.poster ?: ""),
    type = PayloadSaveItemType.ALBUM
)

internal fun List<Pair<ArtistId, List<SongId>>>.toDtoSuggestedArtistSong() =
    this.map { (artistId, list) ->
        DtoRelationSuggestedArtistSong(
            artistId = artistId,
            list = list
        )
    }

internal fun DataError.Network.toWorkResult() = when (this) {
    DataError.Network.UNAUTHORISED,
    DataError.Network.EMAIL_NOT_VERIFIED,
    DataError.Network.PASSWORD_DOES_NOT_MATCH,
    DataError.Network.SERIALISATION,
    DataError.Network.UNKNOWN,
    DataError.Network.CONFLICT,
    DataError.Network.INVALID_EMAIL
        -> ListenableWorker.Result.failure()

    DataError.Network.NOT_FOUND,
    DataError.Network.NO_INTERNET,
    DataError.Network.SERVER_ERROR,
        -> ListenableWorker.Result.retry()
}