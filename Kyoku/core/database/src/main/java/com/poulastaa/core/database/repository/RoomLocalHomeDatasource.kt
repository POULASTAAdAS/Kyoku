package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.mapper.toDtoPrevAlbum
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.database.mapper.toDtoPrevSong
import com.poulastaa.core.database.mapper.toEntityArtist
import com.poulastaa.core.database.mapper.toEntityExploreType
import com.poulastaa.core.database.mapper.toEntityPlaylist
import com.poulastaa.core.database.mapper.toEntityPrevAlbum
import com.poulastaa.core.database.mapper.toEntityPrevArtist
import com.poulastaa.core.database.mapper.toEntityPrevExploreType
import com.poulastaa.core.database.mapper.toEntityPrevSong
import com.poulastaa.core.database.mapper.toEntityRelationAlbum
import com.poulastaa.core.database.mapper.toEntityRelationSongAlbum
import com.poulastaa.core.database.mapper.toEntityRelationSongPlaylist
import com.poulastaa.core.database.mapper.toEntityRelationSuggestedSongByArtist
import com.poulastaa.core.database.mapper.toEntitySavedAlbum
import com.poulastaa.core.database.mapper.toEntitySong
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoRelationSongAlbum
import com.poulastaa.core.domain.model.DtoRelationSongPlaylist
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.repository.LocalHomeDatasource
import javax.inject.Inject
import kotlin.random.Random

internal class RoomLocalHomeDatasource @Inject constructor(
    private val root: RootDao,
    private val home: HomeDao,
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

    override suspend fun storeSavedAlbum(album: DtoAlbum): AlbumId =
        root.insertSavedAlbum(album.toEntitySavedAlbum())

    override suspend fun storeSavedAlbum(list: List<DtoAlbum>): List<AlbumId> =
        root.insertSavedAlbum(list.map { it.toEntitySavedAlbum() })

    override suspend fun storeRelationAlbum(album: DtoAlbum): AlbumId =
        root.insertRelationAlbum(album.toEntityRelationAlbum())

    override suspend fun storeRelationAlbum(list: List<DtoAlbum>): List<AlbumId> =
        root.insertRelationAlbum(list.map { it.toEntityRelationAlbum() })

    override suspend fun storePrevAlbum(album: DtoPrevAlbum): AlbumId =
        root.insertPrevAlbum(album.toEntityPrevAlbum())

    override suspend fun storePrevAlbum(list: List<DtoPrevAlbum>): List<AlbumId> =
        root.insertPrevAlbum(list.map { it.toEntityPrevAlbum() })

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

    override suspend fun storeRelationSuggestedArtistSong(list: List<DtoRelationSuggestedArtistSong>) =
        root.insertRelationSuggestedSongByArtist(
            list.map { it.toEntityRelationSuggestedSongByArtist() }
                .flatten()
        )

    override suspend fun getSavedPrevAlbum(limit: Int): List<DtoPrevAlbum> =
        home.getRandomSavedPrevAlbum(limit).map { it.toDtoPrevAlbum() }

    override suspend fun getSavedPrevArtist(limit: Int): List<DtoPrevArtist> =
        home.getRandomSavedPrevArtist(limit).map { it.toDtoPrevArtist() }

    override suspend fun getSavedPrevPlaylist(limit: Int): List<DtoPrevPlaylist> {
        val playlist = home.getRandomSavedPrevPlaylist(limit).groupBy { it.id }

        return playlist.map { entry ->
            DtoPrevPlaylist(
                id = entry.key,
                title = entry.value.first().title,
                posters = entry.value.map { it.poster }.shuffled(Random).take(4)
            )
        }
    }

    override suspend fun getPrevExploreType(type: DtoExploreType): List<DtoPrevSong> {
        val songIdList = home.getPrevExploreType(type.id)
        return home.getPrevSong(songIdList)
    }

    override suspend fun getSuggestedAlbum(): List<DtoPrevAlbum> = home.getPrevAlbum().map {
        it.toDtoPrevAlbum()
    }

    override suspend fun getSuggestedArtist(): List<DtoPrevArtist> = home.getPrevArtist().map {
        it.toDtoPrevArtist()
    }

    override suspend fun getSuggestedArtistSong(): Map<DtoPrevArtist, List<DtoPrevSong>> =
        home.getSuggestedArtistSong().associate { dto ->
            dto.artist.toDtoPrevArtist() to dto.list.map { it.toDtoPrevSong() }
        }
}