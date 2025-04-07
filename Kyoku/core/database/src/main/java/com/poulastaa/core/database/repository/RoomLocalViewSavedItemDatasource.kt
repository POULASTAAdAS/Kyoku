package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.mapper.toDtoAlbum
import com.poulastaa.core.database.mapper.toDtoArtist
import com.poulastaa.core.database.mapper.toDtoPlaylist
import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylist
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

    override suspend fun getSavedPlaylist(): Map<Pair<List<String>, Int>, DtoPlaylist> =
        viewDao.getSavedPlaylist().associate {
            val payload = scope.async { viewDao.getSavedPlaylistCover(it.id) }
            val songs = scope.async { viewDao.countPlaylistSongs(it.id) }
            Pair(payload.await(), songs.await()) to it.toDtoPlaylist()
        }
}