package com.poulastaa.core.database.repository.sync

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.sync.LocalSyncCacheDatasource

internal class RedisLocalSyncDatasource(
    private val core: LocalCoreCacheDatasource,
) : LocalSyncCacheDatasource {
    override fun setSongById(list: List<DtoSong>) = core.setSongById(list)

    override fun cacheSongById(list: List<SongId>): List<DtoSong> = core.cacheSongById(list)

    override fun setAlbumById(list: List<DtoAlbum>) = core.setAlbumById(list)

    override fun cacheAlbumById(list: List<AlbumId>): List<DtoAlbum> = core.cacheAlbumById(list)

    override fun setPlaylistById(list: List<DtoPlaylist>) = core.setPlaylistOnId(list)

    override fun cachePlaylistOnId(list: List<PlaylistId>): List<DtoPlaylist> = core.cachePlaylistOnId(list)

    override fun setArtistById(list: List<DtoArtist>) = core.setArtistById(list)

    override fun cacheArtistById(list: List<ArtistId>): List<DtoArtist> = core.cacheArtistById(list)

    override fun cacheSongIdByPlaylistId(playlistId: PlaylistId): List<SongId> =
        core.cacheSongIdByPlaylistId(playlistId)
            ?.split(",")
            ?.map { it.toLongOrNull() }
            ?.mapNotNull { it } ?: emptyList()

    override fun setSongIdByPlaylistId(
        playlistId: PlaylistId,
        list: List<SongId>,
    ) = core.setSongIdByPlaylistId(playlistId, list)
}