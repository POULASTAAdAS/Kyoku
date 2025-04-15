package com.poulastaa.core.database.repository.item

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.item.LocalItemCacheDatasource

internal class RedisLocalItemDatasource(
    private val core: LocalCoreCacheDatasource,
) : LocalItemCacheDatasource {
    override fun setArtistById(artist: DtoArtist) = core.setArtistById(artist)
    override fun cacheArtistById(artistId: ArtistId): DtoArtist? = core.cacheArtistById(artistId)

    override fun setPlaylistById(playlist: DtoPlaylist) = core.setPlaylistOnId(playlist)
}