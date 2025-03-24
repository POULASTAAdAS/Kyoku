package com.poulastaa.core.database.repository.sync

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.sync.LocalSyncCacheDatasource
import com.poulastaa.core.domain.repository.sync.LocalSyncDatasource

internal class ExposedLocalSyncDatasource(
    private val core: LocalCoreDatasource,
    private val cache: LocalSyncCacheDatasource,
) : LocalSyncDatasource {
    override suspend fun getUsersByEmail(email: String, type: UserType): DtoDBUser? =
        core.getUserByEmail(email, type)

    override suspend fun getSavedAlbumIdList(userId: Long): List<AlbumId> {
        val cache = cache
    }

    override suspend fun getFullAlbumOnIdList(idList: List<AlbumId>, userId: Long): List<DtoFullAlbum> {
        TODO("Not yet implemented")
    }

    override fun removeAlbum(idList: List<AlbumId>, userId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedPlaylistIdList(userId: Long): List<PlaylistId> {
        TODO("Not yet implemented")
    }

    override suspend fun getFullPlaylistOnfIdList(idList: List<PlaylistId>, userId: Long): List<DtoFullPlaylist> {
        TODO("Not yet implemented")
    }

    override fun removePlaylist(idList: List<PlaylistId>, userId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedArtistIdList(userId: Long): List<ArtistId> {
        TODO("Not yet implemented")
    }

    override suspend fun getArtistOnIdList(idList: List<ArtistId>, userId: Long): List<DtoArtist> {
        TODO("Not yet implemented")
    }

    override fun removeArtist(idList: List<ArtistId>, userId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedFavouriteSongsIdList(userId: Long): List<SongId> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteSongs(idList: List<SongId>, userId: Long): List<DtoSong> {
        TODO("Not yet implemented")
    }

    override fun removeFavouriteSongs(idList: List<SongId>, userId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaylistSongIdList(playlistId: PlaylistId): List<SongId> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaylistSongs(list: List<SongId>): List<DtoSong> {
        TODO("Not yet implemented")
    }

    override fun removePlaylistSongs(playlistId: PlaylistId, songIds: List<SongId>) {
        TODO("Not yet implemented")
    }
}