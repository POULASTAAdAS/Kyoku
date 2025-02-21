package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.mapper.toEntityAlbum
import com.poulastaa.core.database.mapper.toEntityArtist
import com.poulastaa.core.database.mapper.toEntityCountry
import com.poulastaa.core.database.mapper.toEntityExploreType
import com.poulastaa.core.database.mapper.toEntityGenre
import com.poulastaa.core.database.mapper.toEntityPlaylist
import com.poulastaa.core.database.mapper.toEntityPrevAlbum
import com.poulastaa.core.database.mapper.toEntityPrevArtist
import com.poulastaa.core.database.mapper.toEntityPrevExploreType
import com.poulastaa.core.database.mapper.toEntityPrevSong
import com.poulastaa.core.database.mapper.toEntityRelationSongAlbum
import com.poulastaa.core.database.mapper.toEntityRelationSongPlaylist
import com.poulastaa.core.database.mapper.toEntityRelationSuggested
import com.poulastaa.core.database.mapper.toEntityRelationSuggestedSongByArtist
import com.poulastaa.core.database.mapper.toEntitySong
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.CountryId
import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoCountry
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoRelationSongAlbum
import com.poulastaa.core.domain.model.DtoRelationSongPlaylist
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoSuggestedType
import com.poulastaa.core.domain.model.GenreId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.repository.LocalHomeDatasource
import javax.inject.Inject

class RoomLocalHomeDatasource @Inject constructor(
    private val root: RootDao,
) : LocalHomeDatasource {
    override suspend fun storeSong(song: DtoSong): SongId =
        root.insertSong(song.toEntitySong())

    override suspend fun storeSong(list: List<DtoSong>): List<SongId> =
        root.insertSong(list.map { it.toEntitySong() })

    override suspend fun storePrevSong(list: List<DtoPrevSong>): List<SongId> =
        root.insertPrevSong(list.map { it.toEntityPrevSong() })

    override suspend fun storeArtist(artist: DtoArtist): ArtistId =
        root.insertArtist(artist.toEntityArtist())

    override suspend fun storeArtist(list: List<DtoArtist>): List<ArtistId> =
        root.insertArtist(list.map { it.toEntityArtist() })

    override suspend fun storePrevArtist(artist: DtoPrevArtist): ArtistId =
        root.insertPrevArtist(artist.toEntityPrevArtist())

    override suspend fun storePrevArtist(list: List<DtoPrevArtist>): List<ArtistId> =
        root.insertPrevArtist(list.map { it.toEntityPrevArtist() })

    override suspend fun storePlaylist(playlist: DtoPlaylist): PlaylistId =
        root.insertPlaylist(playlist.toEntityPlaylist())

    override suspend fun storePlaylist(list: List<DtoPlaylist>): List<PlaylistId> =
        root.insertPlaylist(list.map { it.toEntityPlaylist() })

    override suspend fun storeAlbum(album: DtoAlbum): AlbumId =
        root.insertAlbum(album.toEntityAlbum())

    override suspend fun storeAlbum(list: List<DtoAlbum>): List<AlbumId> =
        root.insertAlbum(list.map { it.toEntityAlbum() })

    override suspend fun storePrevAlbum(album: DtoPrevAlbum): AlbumId =
        root.insertPrevAlbum(album.toEntityPrevAlbum())

    override suspend fun storePrevAlbum(list: List<DtoPrevAlbum>): List<AlbumId> =
        root.insertPrevAlbum(list.map { it.toEntityPrevAlbum() })

    override suspend fun storeGenre(genre: DtoGenre): Long =
        root.insertGenre(genre.toEntityGenre())

    override suspend fun storeGenre(list: List<DtoGenre>): List<Long> =
        root.insertGenre(list.map { it.toEntityGenre() })

    override suspend fun storeCountry(country: DtoCountry): Long =
        root.insertCountry(country.toEntityCountry())

    override suspend fun storeCountry(list: List<DtoCountry>): List<Long> =
        root.insertCountry(list.map { it.toEntityCountry() })

    override suspend fun storeExploreType(type: DtoExploreType, data: List<SongId>) =
        root.insertExploreType(type.toEntityExploreType(data))

    override suspend fun storePrevExploreType(type: DtoExploreType, data: List<SongId>) =
        root.insertPrevExploreType(type.toEntityPrevExploreType(data))

    override suspend fun storeRelationSongPlaylist(relation: DtoRelationSongPlaylist) =
        root.insertRelationSongPlaylist(relation.toEntityRelationSongPlaylist())

    override suspend fun storeRelationSongPlaylist(list: List<DtoRelationSongPlaylist>) =
        root.insertRelationSongPlaylist(
            list.map {
                it.toEntityRelationSongPlaylist()
            }.flatten()
        )

    override suspend fun stoRelationSongAlbum(relation: DtoRelationSongAlbum) =
        root.insertRelationSongAlbum(relation.toEntityRelationSongAlbum())

    override suspend fun stoRelationSongAlbum(list: List<DtoRelationSongAlbum>) =
        root.insertRelationSongAlbum(
            list.map {
                it.toEntityRelationSongAlbum()
            }.flatten()
        )

    override suspend fun storeRelationSuggested(type: DtoSuggestedType, data: List<Long>) {
        root.insertRelationSuggested(type.toEntityRelationSuggested(data))
    }

    override suspend fun storeRelationSuggestedArtistSong(list: List<DtoRelationSuggestedArtistSong>) =
        root.insertRelationSuggestedSongByArtist(
            list.map { it.toEntityRelationSuggestedSongByArtist() }
                .flatten()
        )
}