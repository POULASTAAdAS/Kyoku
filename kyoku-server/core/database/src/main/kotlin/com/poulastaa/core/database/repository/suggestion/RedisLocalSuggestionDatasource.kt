package com.poulastaa.core.database.repository.suggestion

import com.google.gson.Gson
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.*
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionCacheDatasource
import redis.clients.jedis.JedisPool

class RedisLocalSuggestionDatasource(
    private val gson: Gson,
    private val redisPool: JedisPool,
    private val core: LocalCoreCacheDatasource,
) : LocalSuggestionCacheDatasource, RedisKeys() {
    override fun setPrevSongById(song: DtoPrevSong) = core.setPrevSongById(song)
    override fun setPrevSongById(list: List<DtoPrevSong>) = core.setPrevSongById(list)
    override fun cachePrevSongById(list: List<SongId>): List<DtoPrevSong> = core.cachePrevSongById(list)
    override fun cachePrevSongById(songId: SongId): DtoPrevSong? = core.cachePrevSongById(songId)

    override fun cachePlaylistOnId(playlistId: PlaylistId): DtoPlaylist? = core.cachePlaylistOnId(playlistId)
    override fun cachePlaylistOnId(list: List<PlaylistId>): List<DtoPlaylist> = core.cachePlaylistOnId(list)
    override fun setPlaylistOnId(data: DtoPlaylist) = core.setPlaylistOnId(data)
    override fun setPlaylistOnId(list: List<DtoPlaylist>) = core.setPlaylistOnId(list)

    override fun cacheAlbumById(albumId: AlbumId): DtoAlbum? = core.cacheAlbumById(albumId)
    override fun cacheAlbumById(list: List<AlbumId>): List<DtoAlbum> = core.cacheAlbumById(list)
    override fun setAlbumById(album: DtoAlbum) = core.setAlbumById(album)
    override fun setAlbumById(list: List<DtoAlbum>) = core.setAlbumById(list)

    override fun cacheArtistById(artistId: ArtistId): DtoArtist? = core.cacheArtistById(artistId)
    override fun cacheArtistById(list: List<ArtistId>): List<DtoArtist> = core.cacheArtistById(list)
    override fun setArtistById(artist: DtoArtist) = core.setArtistById(artist)
    override fun setArtistById(list: List<DtoArtist>) = core.setArtistById(list)

    override fun cacheDetailedPrevSongById(
        songId: SongId,
    ): DtoDetailedPrevSong? = core.cacheDetailedPrevSongById(songId)

    override fun cacheDetailedPrevSongById(
        list: List<SongId>,
    ): List<DtoDetailedPrevSong> = core.cacheDetailedPrevSongById(list)
}