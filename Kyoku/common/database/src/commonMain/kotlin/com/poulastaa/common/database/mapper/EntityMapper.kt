package com.poulastaa.common.database.mapper

import com.poulastaa.common.database.entity.AlbumArtistEntity
import com.poulastaa.common.database.entity.AlbumEntity
import com.poulastaa.common.database.entity.ArtistEntity
import com.poulastaa.common.database.entity.ComposerEntity
import com.poulastaa.common.database.entity.CountryEntity
import com.poulastaa.common.database.entity.GenreEntity
import com.poulastaa.common.database.entity.PlaylistEntity
import com.poulastaa.common.database.entity.PlaylistVisibilityEntity
import com.poulastaa.common.database.entity.SongEntity
import com.poulastaa.common.database.entity.relation.SongWithRelations
import com.poulastaa.common.database.entity.relation.SongWithRelationsBundle
import com.poulastaa.common.domain.model.DtoAlbum
import com.poulastaa.common.domain.model.DtoArtist
import com.poulastaa.common.domain.model.DtoComposer
import com.poulastaa.common.domain.model.DtoCountry
import com.poulastaa.common.domain.model.DtoGenre
import com.poulastaa.common.domain.model.DtoPlaylist
import com.poulastaa.common.domain.model.DtoPlaylistVisibility
import com.poulastaa.common.domain.model.DtoSong
import com.poulastaa.common.domain.model.DtoSongInfo

fun DtoSong.toBundle(): SongWithRelationsBundle {
    val songArtistIds = artists.map { it.id }.toSet()

    return SongWithRelationsBundle(
        song = SongEntity(
            id = id,
            title = title,
            poster = poster,
            masterPlaylist = masterPlaylist,
            releaseYear = info?.releaseYear?.takeIf { it > 0 },
            popularity = info?.popularity,
        ),
        artists = artists.map { it.toEntity() },
        albumOnlyArtists = albums.flatMap { it.artists }
            .filterNot { it.id in songArtistIds }
            .map { it.toEntity() },
        albums = albums.map { it.toEntity() },
        albumArtists = albums.flatMap { album ->
            album.artists.map { AlbumArtistEntity(albumId = album.id, artistId = it.id) }
        },
        genres = genres.map { it.toEntity() },
        countries = countries.map { it.toEntity() },
        composers = info?.composers.orEmpty().map { it.toEntity() },
    )
}

fun DtoPlaylist.toEntity(spotifyPlaylistId: String? = null) = PlaylistEntity(
    id = id,
    name = name,
    popularity = popularity,
    visibility = when (visibility) {
        DtoPlaylistVisibility.PUBLIC -> PlaylistVisibilityEntity.PUBLIC
        DtoPlaylistVisibility.PRIVATE -> PlaylistVisibilityEntity.PRIVATE
    },
    spotifyPlaylistId = spotifyPlaylistId,
)

fun DtoArtist.toEntity() = ArtistEntity(
    id = id,
    name = name,
    coverImage = coverImage,
    followers = followers,
    monthlyListeners = monthlyListeners,
    birthDate = birthDate,
    biography = biography,
)

fun DtoAlbum.toEntity() = AlbumEntity(
    id = id,
    name = name,
    poster = poster,
    popularity = popularity,
)

fun DtoGenre.toEntity() = GenreEntity(
    id = id,
    name = name,
    coverImage = coverImage,
    popularity = popularity,
)

fun DtoCountry.toEntity() = CountryEntity(
    id = id,
    name = name,
    code = code,
)

fun DtoComposer.toEntity() = ComposerEntity(
    id = id,
    name = name,
    coverImage = coverImage,
    followers = followers,
)

fun PlaylistEntity.toDto() = DtoPlaylist(
    id = id,
    name = name,
    popularity = popularity,
    visibility = when (visibility) {
        PlaylistVisibilityEntity.PUBLIC -> DtoPlaylistVisibility.PUBLIC
        PlaylistVisibilityEntity.PRIVATE -> DtoPlaylistVisibility.PRIVATE
    },
)

fun SongWithRelations.toDto() = DtoSong(
    id = song.id,
    title = song.title,
    poster = song.poster,
    masterPlaylist = song.masterPlaylist,
    info = DtoSongInfo(
        songId = song.id,
        releaseYear = song.releaseYear ?: 0,
        composers = composers.map { it.toDto() },
        popularity = song.popularity ?: 0,
    ),
    artists = artists.map { it.toDto() },
    genres = genres.map { it.toDto() },
    countries = countries.map { it.toDto() },
    albums = albums.map { it.toDto() },
)

fun ArtistEntity.toDto() = DtoArtist(
    id = id,
    name = name,
    coverImage = coverImage,
    followers = followers,
    birthDate = birthDate,
    biography = biography,
    monthlyListeners = monthlyListeners,
)

fun AlbumEntity.toDto() = DtoAlbum(
    id = id,
    name = name,
    popularity = popularity,
    poster = poster,
)

fun GenreEntity.toDto() = DtoGenre(
    id = id,
    name = name,
    coverImage = coverImage,
    popularity = popularity,
)

fun CountryEntity.toDto() = DtoCountry(
    id = id,
    name = name,
    code = code,
)

fun ComposerEntity.toDto() = DtoComposer(
    id = id,
    name = name,
    coverImage = coverImage,
    followers = followers,
)
