package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.mapper.toDtoPrevAlbum
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import com.poulastaa.core.domain.repository.LocalLibraryDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

internal class RoomLocalLibraryDatasource @Inject constructor(
    private val library: LibraryDao,
) : LocalLibraryDatasource {
    override suspend fun loadSavedPlaylist(): Flow<List<DtoPrevPlaylist>> {
        val payload = library.loadSavedPrevPlaylist().map { list ->
            list.groupBy { it.id }
        }

        return payload.map { list ->
            list.map { entry ->
                DtoPrevPlaylist(
                    id = entry.key,
                    title = entry.value.first().title,
                    posters = entry.value.map { it.poster }.shuffled(Random).take(4)
                )
            }
        }
    }

    override fun loadSavedArtist(): Flow<List<DtoPrevArtist>> =
        library.loadSavedPrevArtist().map { list ->
            list.map { entity ->
                entity.toDtoPrevArtist()
            }
        }

    override fun loadSavedAlbum(): Flow<List<DtoPrevAlbum>> =
        library.loadSavedPrevAlbum().map { list ->
            list.map { entity ->
                entity.toDtoPrevAlbum()
            }
        }
}