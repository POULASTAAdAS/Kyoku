package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.mapper.toDtoAlbum
import com.poulastaa.core.database.mapper.toDtoArtist
import com.poulastaa.core.database.mapper.toDtoPlaylist
import com.poulastaa.core.database.mapper.toDtoPlaylistPayload
import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylistPayload
import com.poulastaa.core.domain.repository.LocalViewSavedItemDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

internal class RoomLocalViewSavedItemDatasource @Inject constructor(
    private val viewDao: ViewDao,
    private val scope: CoroutineScope,
) : LocalViewSavedItemDatasource {
    override suspend fun getSavedArtist(): List<DtoArtist> =
        viewDao.getSavedArtist().map { it.toDtoArtist() }

    override suspend fun getSavedAlbum(): Map<String?, DtoAlbum> =
        viewDao.getSavedAlbums().associate {
            val artist = viewDao.getArtistOnAlbumId(it.id).joinToString(",")
            (if (artist.isBlank()) null else artist) to it.toDtoAlbum()
        }

    override suspend fun getSavedPlaylist(): List<DtoPlaylistPayload> =
        viewDao.getSavedPlaylist().map { it.toDtoPlaylist() }.map {
            val coversDef = scope.async { viewDao.getSavedPlaylistCover(it.id) }
            val countDef = scope.async { viewDao.countPlaylistSongs(it.id) }

            val covers = coversDef.await()
            val count = countDef.await()

            it.toDtoPlaylistPayload(covers, count)
        }
}