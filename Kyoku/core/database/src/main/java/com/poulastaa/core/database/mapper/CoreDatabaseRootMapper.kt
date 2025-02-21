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
import com.poulastaa.core.database.entity.EntityRelationSuggested
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
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoRelationSongAlbum
import com.poulastaa.core.domain.model.DtoRelationSongPlaylist
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoSongInfo
import com.poulastaa.core.domain.model.DtoSuggestedType
import com.poulastaa.core.domain.model.SongId

fun DtoSong.toEntitySong() = EntitySong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    masterPlaylist = this.masterPlaylist
)

fun DtoPlaylist.toEntityPlaylist() = EntityPlaylist(
    id = this.id,
    name = this.name,
    visibilityState = this.visibilityState,
    popularity = this.popularity
)

fun DtoSongInfo.toSongInfo() = EntitySongInfo(
    songId = this.songId,
    releaseYear = this.releaseYear,
    popularity = this.popularity,
    composer = this.composer
)

fun DtoArtist.toEntityArtist() = EntityArtist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity
)

fun DtoGenre.toEntityGenre() = EntityGenre(
    id = this.id,
    name = this.name,
    cover = this.cover
)

fun DtoAlbum.toEntityAlbum() = EntityAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster,
    popularity = this.popularity
)

fun DtoCountry.toEntityCountry() = EntityCountry(
    id = this.id,
    name = this.name,
)

fun DtoPrevSong.toEntityPrevSong() = EntityPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
)

fun DtoPrevArtist.toEntityPrevArtist() = EntityPrevArtist(
    id = this.id,
    name = this.name,
    coverImage = this.cover,
)

fun DtoPrevAlbum.toEntityPrevAlbum() = EntityPrevAlbum(
    id = this.id,
    title = this.name,
    poster = this.poster
)


fun EntityPlaylist.toDtoPlaylist() = DtoPlaylist(
    id = this.id,
    name = this.name,
    visibilityState = this.visibilityState,
    popularity = this.popularity
)

fun EntitySong.toDtoDetailedPrevSong(
    artists: List<SongIdWithArtistName>,
    releaseYears: Map<SongId, Int>,
) = DtoDetailedPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artists = artists.firstOrNull { it.songId == this.id }?.artist,
    releaseYear = releaseYears[this.id] ?: -1
)

fun PlaylistWithSong.toDtoPrevPlaylist(
    artists: List<SongIdWithArtistName>,
    releaseYears: Map<SongId, Int>,
) = DtoPrevPlaylist(
    playlist = this.playlist.toDtoPlaylist(),
    list = this.list.map { it.toDtoDetailedPrevSong(artists, releaseYears) }
)

@JvmName("listExploreType")
fun DtoExploreType.toEntityExploreType(list: List<SongId>) = list.map { songId ->
    this.toEntityExploreType(songId)
}

@JvmName("singleExploreType")
fun DtoExploreType.toEntityExploreType(songId: SongId) = EntityExplore(
    typeId = this,
    dataId = songId
)


@JvmName("listPrevExploreType")
fun DtoExploreType.toEntityPrevExploreType(list: List<SongId>) = list.map { songId ->
    this.toEntityPrevExploreType(songId)
}

@JvmName("singlePrevExploreType")
fun DtoExploreType.toEntityPrevExploreType(songId: SongId) = EntityPrevExplore(
    typeId = this,
    dataId = songId
)

@JvmName("listRelationSuggested")
fun DtoSuggestedType.toEntityRelationSuggested(list: List<Long>) = list.map { dataId ->
    this.toEntityRelationSuggested(dataId)
}

@JvmName("singleRelationSuggested")
fun DtoSuggestedType.toEntityRelationSuggested(dataId: Long) = EntityRelationSuggested(
    typeId = this,
    dataId = dataId
)

fun DtoRelationSuggestedArtistSong.toEntityRelationSuggestedSongByArtist() = list.map { songId ->
    EntityRelationSuggestedSongByArtist(
        artistId = this.artistId,
        songId = songId
    )
}

fun DtoRelationSongPlaylist.toEntityRelationSongPlaylist() = this.list.map { songId ->
    EntityRelationSongPlaylist(
        playlistId = this.playlistId,
        songId = songId
    )
}

fun DtoRelationSongAlbum.toEntityRelationSongAlbum() = this.list.map { ialbumId ->
    EntityRelationSongAlbum(
        albumId = this.albumId,
        songId = ialbumId
    )
}