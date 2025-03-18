package com.poulastaa.core.domain.repository

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

interface LocalHomeDatasource {
    suspend fun storeSong(song: DtoSong): SongId
    suspend fun storeSong(list: List<DtoSong>): List<SongId>

    suspend fun storePrevSong(list: List<DtoPrevSong>): List<SongId>

    suspend fun storeArtist(artist: DtoArtist): ArtistId
    suspend fun storeArtist(list: List<DtoArtist>): List<ArtistId>

    suspend fun storePrevArtist(artist: DtoPrevArtist): ArtistId
    suspend fun storePrevArtist(list: List<DtoPrevArtist>): List<ArtistId>

    suspend fun storePlaylist(playlist: DtoPlaylist): PlaylistId
    suspend fun storePlaylist(list: List<DtoPlaylist>): List<PlaylistId>

    suspend fun storeSavedAlbum(album: DtoAlbum): AlbumId
    suspend fun storeSavedAlbum(list: List<DtoAlbum>): List<AlbumId>

    suspend fun storeRelationAlbum(album: DtoAlbum): AlbumId
    suspend fun storeRelationAlbum(list: List<DtoAlbum>): List<AlbumId>

    suspend fun storePrevAlbum(album: DtoPrevAlbum): AlbumId
    suspend fun storePrevAlbum(list: List<DtoPrevAlbum>): List<AlbumId>

    suspend fun storeExploreType(type: DtoExploreType, data: List<SongId>)
    suspend fun storePrevExploreType(type: DtoExploreType, data: List<SongId>)

    suspend fun storeRelationSongPlaylist(relation: DtoRelationSongPlaylist)
    suspend fun storeRelationSongPlaylist(list: List<DtoRelationSongPlaylist>)

    suspend fun stoRelationSongAlbum(relation: DtoRelationSongAlbum)
    suspend fun stoRelationSongAlbum(list: List<DtoRelationSongAlbum>)

    suspend fun storeRelationSuggestedArtistSong(list: List<DtoRelationSuggestedArtistSong>)

    suspend fun getSavedPrevAlbum(limit: Int = 2): List<DtoPrevAlbum>
    suspend fun getSavedPrevArtist(limit: Int = 2): List<DtoPrevArtist>
    suspend fun getSavedPrevPlaylist(limit: Int = 2): List<DtoPrevPlaylist>

    suspend fun getPrevExploreType(type: DtoExploreType): List<DtoPrevSong>

    suspend fun getSuggestedAlbum(): List<DtoPrevAlbum>
    suspend fun getSuggestedArtist(): List<DtoPrevArtist>
    suspend fun getSuggestedArtistSong(): Map<DtoPrevArtist, List<DtoPrevSong>>
}