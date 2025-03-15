package com.poulastaa.core.database.repository.view

import com.google.gson.Gson
import com.poulastaa.core.database.mapper.toDtoDetailedPrevSong
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.view.LocalViewCacheDatasource
import redis.clients.jedis.JedisPool

internal class RedisLocalViewDatasource(
    private val gson: Gson,
    private val redisPool: JedisPool,
    private val core: LocalCoreCacheDatasource,
) : LocalViewCacheDatasource {
    override fun cacheArtistById(artistId: ArtistId): DtoPrevArtist? =
        core.cacheArtistById(artistId)?.toDtoPrevArtist() ?: core.cachePrevArtistById(artistId)

    override fun setArtistById(artist: DtoPrevArtist) = core.setPrevArtistById(artist)
    override fun cacheDetailedPrevSongById(list: List<SongId>): List<DtoDetailedPrevSong> =
        core.cacheDetailedPrevSongById(list)

    override fun setDetailedPrevSongById(songs: List<DtoDetailedPrevSong>) = core.setDetailedPrevSongById(songs)

    override fun cachePlaylistOnId(playlistId: PlaylistId): DtoPlaylist? = core.cachePlaylistOnId(playlistId)
    override fun setPlaylistOnId(playlist: DtoPlaylist) = core.setPlaylistOnId(playlist)

    override fun cachePrevDetailedSongByPlaylistId(playlistId: PlaylistId): Pair<List<DtoDetailedPrevSong>, List<SongId>>? {
        val listOfSongId = core.cacheSongIdByPlaylistId(playlistId)?.let {
            it.split(",").map { it.toLong() as SongId }
        } ?: return null

        return getDtoDetailedPrevSongOnSongIds(listOfSongId)
    }

    override fun setSongIdByPlaylistId(playlistId: PlaylistId, list: List<SongId>) =
        core.setSongIdByPlaylistId(playlistId, list)

    override fun cachePrevDetailedSongByAlbumId(albumId: AlbumId): Pair<List<DtoDetailedPrevSong>, List<SongId>>? {
        val listOfSongId = core.cacheSongIdByAlbumId(albumId)?.let {
            it.split(",").map { it.toLong() as SongId }
        } ?: return null

        return getDtoDetailedPrevSongOnSongIds(listOfSongId)
    }

    override fun setSongIdByAlbumId(albumId: AlbumId, list: List<SongId>) = core.setSongIdByAlbumId(albumId, list)

    override fun cacheAlbumById(albumId: AlbumId): DtoAlbum? = core.cacheAlbumById(albumId)
    override fun setAlbumById(album: DtoAlbum) = core.setAlbumById(album)

    override fun cacheUserFevPrevSong(userId: Long): Pair<List<DtoDetailedPrevSong>, List<SongId>>? {
        val listOfSongId = core.cacheUserFevSongId(userId)?.let {
            it.split(",").map { it.toLong() as SongId }
        } ?: return null

        return getDtoDetailedPrevSongOnSongIds(listOfSongId)
    }

    override fun setUserFevPrevSong(userId: Long, list: List<SongId>) = core.setUserFevSongId(userId, list)

    override fun cacheSongById(list: List<SongId>): List<DtoSong> = core.cacheSongById(list)
    override fun setSongById(list: List<DtoSong>) = core.setSongById(list)

    private fun getDtoDetailedPrevSongOnSongIds(listOfSongId: List<SongId>): Pair<List<DtoDetailedPrevSong>, List<SongId>> {
        val songs = core.cacheSongById(listOfSongId).map { it.toDtoDetailedPrevSong() }

        val ids = songs.map { it.id }
        val prevSongs = listOfSongId.filterNot { it in ids }.let { core.cacheDetailedPrevSongById(it) }

        val allIds = (songs + prevSongs).map { it.id }
        val notFoundIds = listOfSongId.filterNot { it in allIds }

        return Pair(songs + prevSongs, notFoundIds)
    }
}