package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.entity.EntityCountry
import com.poulastaa.core.database.entity.EntityExplore
import com.poulastaa.core.database.entity.EntityGenre
import com.poulastaa.core.database.entity.EntityPlaylist
import com.poulastaa.core.database.entity.EntityPrevAlbum
import com.poulastaa.core.database.entity.EntityPrevArtist
import com.poulastaa.core.database.entity.EntityPrevExplore
import com.poulastaa.core.database.entity.EntityPrevSong
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.entity.EntityRelationSuggestedSongByArtist
import com.poulastaa.core.database.entity.EntitySong
import com.poulastaa.core.database.entity.EntitySongInfo
import com.poulastaa.core.database.relation.PlaylistWithSong
import com.poulastaa.core.database.relation.SongIdWithArtistName
import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoCountry
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevFullPlaylist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoRelationSongAlbum
import com.poulastaa.core.domain.model.DtoRelationSongPlaylist
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoSongInfo
import com.poulastaa.core.domain.model.SongId

internal fun DtoSong.toEntitySong() = EntitySong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    masterPlaylist = this.masterPlaylist
)

internal fun DtoPlaylist.toEntityPlaylist() = EntityPlaylist(
    id = this.id,
    name = this.name,
    visibilityState = this.visibilityState,
    popularity = this.popularity
)

internal fun DtoSongInfo.toSongInfo() = EntitySongInfo(
    songId = this.songId,
    releaseYear = this.releaseYear,
    popularity = this.popularity,
    composer = this.composer
)

internal fun DtoArtist.toEntityArtist() = EntityArtist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity
)

internal fun DtoGenre.toEntityGenre() = EntityGenre(
    id = this.id,
    name = this.name,
    cover = this.cover
)

internal fun DtoAlbum.toEntityAlbum() = EntityAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster,
    popularity = this.popularity
)

internal fun DtoCountry.toEntityCountry() = EntityCountry(
    id = this.id,
    name = this.name,
)

internal fun DtoPrevSong.toEntityPrevSong() = EntityPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
)

internal fun DtoPrevArtist.toEntityPrevArtist() = EntityPrevArtist(
    id = this.id,
    name = this.name,
    coverImage = this.cover,
)

internal fun DtoPrevAlbum.toEntityPrevAlbum() = EntityPrevAlbum(
    id = this.id,
    title = this.name,
    poster = this.poster
)


internal fun EntityPlaylist.toDtoPlaylist() = DtoPlaylist(
    id = this.id,
    name = this.name,
    visibilityState = this.visibilityState,
    popularity = this.popularity
)

internal fun EntitySong.toDtoDetailedPrevSong(
    artists: List<SongIdWithArtistName>,
    releaseYears: Map<SongId, Int>,
) = DtoDetailedPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artists = artists.firstOrNull { it.songId == this.id }?.artist,
    releaseYear = releaseYears[this.id] ?: -1
)

internal fun PlaylistWithSong.toDtoPrevPlaylist(
    artists: List<SongIdWithArtistName>,
    releaseYears: Map<SongId, Int>,
) = DtoPrevFullPlaylist(
    playlist = this.playlist.toDtoPlaylist(),
    list = this.list.map { it.toDtoDetailedPrevSong(artists, releaseYears) }
)

@JvmName("listExploreType")
internal fun DtoExploreType.toEntityExploreType(list: List<SongId>) = list.map { songId ->
    this.toEntityExploreType(songId)
}

@JvmName("singleExploreType")
internal fun DtoExploreType.toEntityExploreType(songId: SongId) = EntityExplore(
    typeId = this,
    dataId = songId
)


@JvmName("listPrevExploreType")
internal fun DtoExploreType.toEntityPrevExploreType(list: List<SongId>) = list.map { songId ->
    this.toEntityPrevExploreType(songId)
}

@JvmName("singlePrevExploreType")
internal fun DtoExploreType.toEntityPrevExploreType(songId: SongId) = EntityPrevExplore(
    typeId = this,
    dataId = songId
)

internal fun DtoRelationSuggestedArtistSong.toEntityRelationSuggestedSongByArtist() =
    list.map { songId ->
        EntityRelationSuggestedSongByArtist(
            artistId = this.artistId,
            songId = songId
        )
    }

internal fun DtoRelationSongPlaylist.toEntityRelationSongPlaylist() = this.list.map { songId ->
    EntityRelationSongPlaylist(
        playlistId = this.playlistId,
        songId = songId
    )
}

internal fun DtoRelationSongAlbum.toEntityRelationSongAlbum() = this.list.map { ialbumId ->
    EntityRelationSongAlbum(
        albumId = this.albumId,
        songId = ialbumId
    )
}

internal fun EntityAlbum.toDtoPrevAlbum() = DtoPrevAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster
)

internal fun EntityArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id,
    name = this.name,
    cover = this.coverImage
)

internal fun EntityPrevArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id,
    name = this.name,
    cover = this.coverImage
)

internal fun EntityPrevAlbum.toDtoPrevAlbum() = DtoPrevAlbum(
    id = this.id,
    name = this.title,
    poster = this.poster
)

internal fun EntityPrevSong.toDtoPrevSong() = DtoPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster
)