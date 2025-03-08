package com.poulastaa.core.database.repository.view

import com.google.gson.Gson
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.domain.repository.view.LocalViewCacheDatasource
import redis.clients.jedis.JedisPool

internal class RedisLocalViewDatasource(
    private val gson: Gson,
    private val redisPool: JedisPool,
    private val core: LocalCoreCacheDatasource,
) : LocalViewCacheDatasource {
    override fun cacheArtistById(artistId: ArtistId): DtoPrevArtist? = core.cacheArtistById(artistId)?.toDtoPrevArtist()
    override fun cacheDetailedPrevSongById(list: List<SongId>): List<DtoDetailedPrevSong> = core.cacheDetailedPrevSongById(list)
    override fun setDetailedPrevSongById(songs: List<DtoDetailedPrevSong>) = core.setDetailedPrevSongById(songs)
}