package com.poulastaa.search.data.repository

import com.poulastaa.core.domain.model.DtoExploreAlbumFilterType
import com.poulastaa.core.domain.model.DtoExploreArtistFilterType
import com.poulastaa.core.domain.model.DtoSearchItem
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.search.LocalPagingDatasource
import com.poulastaa.search.data.mapper.toDtoAddSongToPlaylistItem
import com.poulastaa.search.model.DtoAddSongToPlaylistItem
import com.poulastaa.search.model.DtoAddSongToPlaylistItemType
import com.poulastaa.search.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.search.repository.PagingRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

internal class PagingRepositoryService(
    private val db: LocalPagingDatasource,
) : PagingRepository {
    override suspend fun getArtistPagingSong(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoSearchItem> = db.getArtistPagingSong(page, size, query, artistId)

    override suspend fun getArtistPagingAlbum(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoSearchItem> = db.getArtistPagingAlbum(page, size, query, artistId)

    override suspend fun getPagingAlbum(
        query: String?,
        page: Int,
        size: Int,
        filterType: DtoExploreAlbumFilterType,
    ): List<DtoSearchItem> = db.getPagingAlbum(query, page, size, filterType)

    override suspend fun getPagingArtist(
        query: String?,
        page: Int,
        size: Int,
        filterType: DtoExploreArtistFilterType,
    ): List<DtoSearchItem> = db.getPagingArtist(query, page, size, filterType)

    override suspend fun getPagingCreatePlaylist(
        page: Int,
        size: Int,
        query: String?,
        filterType: DtoAddSongToPlaylistSearchFilterType,
    ): List<DtoAddSongToPlaylistItem> = when (filterType) {
        DtoAddSongToPlaylistSearchFilterType.ALL -> coroutineScope {
            val pageSize = size / 4

            val albumDef = async {
                db.getPagingAlbum(
                    query = query,
                    page = page,
                    size = pageSize,
                    filterType = DtoExploreAlbumFilterType.MOST_POPULAR
                ).map { it.toDtoAddSongToPlaylistItem(DtoAddSongToPlaylistItemType.ALBUM) }
            }
            val artistDef = async {
                db.getPagingArtist(
                    query = query,
                    page = page,
                    size = pageSize,
                    filterType = DtoExploreArtistFilterType.ALL
                ).map { it.toDtoAddSongToPlaylistItem(DtoAddSongToPlaylistItemType.ARTIST) }
            }
            val songDef = async {
                db.getPagingSong(
                    query = query,
                    page = page,
                    size = pageSize
                ).map { it.toDtoAddSongToPlaylistItem(DtoAddSongToPlaylistItemType.SONG) }
            }
            val playlistDef = async {
                db.getPagingPlaylist(
                    query = query,
                    page = page,
                    size = pageSize
                ).map { it.toDtoAddSongToPlaylistItem(DtoAddSongToPlaylistItemType.PLAYLIST) }
            }

            listOf(
                albumDef,
                artistDef,
                songDef,
                playlistDef
            ).awaitAll().flatten().shuffled(Random)
        }

        DtoAddSongToPlaylistSearchFilterType.ALBUM -> db.getPagingAlbum(
            query = query,
            page = page,
            size = size,
            filterType = DtoExploreAlbumFilterType.MOST_POPULAR
        ).map { it.toDtoAddSongToPlaylistItem(DtoAddSongToPlaylistItemType.ALBUM) }

        DtoAddSongToPlaylistSearchFilterType.SONG -> db.getPagingSong(
            query = query,
            page = page,
            size = size
        ).map { it.toDtoAddSongToPlaylistItem(DtoAddSongToPlaylistItemType.SONG) }

        DtoAddSongToPlaylistSearchFilterType.ARTIST -> db.getPagingArtist(
            query = query,
            page = page,
            size = size,
            filterType = DtoExploreArtistFilterType.ALL
        ).map { it.toDtoAddSongToPlaylistItem(DtoAddSongToPlaylistItemType.ARTIST) }

        DtoAddSongToPlaylistSearchFilterType.PLAYLIST -> db.getPagingPlaylist(
            query = query,
            page = page,
            size = size
        ).map { it.toDtoAddSongToPlaylistItem(DtoAddSongToPlaylistItemType.PLAYLIST) }
    }
}