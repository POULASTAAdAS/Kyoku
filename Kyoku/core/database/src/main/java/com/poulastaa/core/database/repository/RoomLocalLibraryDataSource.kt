package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.entity.PinnedEntity
import com.poulastaa.core.database.mapper.toArtist
import com.poulastaa.core.database.mapper.toPinnedData
import com.poulastaa.core.database.mapper.toPinnedType
import com.poulastaa.core.database.mapper.toSavedPlaylist
import com.poulastaa.core.domain.PinReqType
import com.poulastaa.core.domain.library.LocalLibraryDataSource
import com.poulastaa.core.domain.model.PinnedData
import com.poulastaa.core.domain.model.PinnedType
import com.poulastaa.core.domain.utils.SavedAlbum
import com.poulastaa.core.domain.utils.SavedArtist
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalLibraryDataSource @Inject constructor(
    private val commonDao: CommonDao,
    private val libraryDao: LibraryDao,
) : LocalLibraryDataSource {
    override fun getPlaylist(): Flow<SavedPlaylist> = commonDao.getAllSavedPlaylist().map {
        it.groupBy { result -> result.id }
            .map { groped -> groped.toSavedPlaylist() }
    }

    override fun getAlbum(): Flow<SavedAlbum> = commonDao.getAllSavedAlbum()

    override fun getArtist(): Flow<SavedArtist> = libraryDao.getAllSavedArtist().map { list ->
        list.map {
            it.toArtist()
        }
    }

    override suspend fun isFavourite(): Boolean = commonDao.isFavourite().isNotEmpty()

    override suspend fun getPinnedData(): Flow<List<PinnedData>> = coroutineScope {
        libraryDao.getPinnedData()
            .map { it.asReversed() }
            .map { entry ->
                entry.map { pinnedEntity ->
                    when (pinnedEntity.type) {
                        PinnedType.PLAYLIST -> libraryDao.getPinnedPlaylist(pinnedEntity.id)
                            .groupBy { it.id }
                            .map { it.toPinnedData(pinnedEntity.type) }.first()

                        PinnedType.ALBUM -> commonDao.getAlbumById(pinnedEntity.id).toPinnedData()

                        PinnedType.ARTIST -> commonDao.getArtistById(pinnedEntity.id).toPinnedData()
                    }
                }
            }
    }

    override suspend fun checkIfPinned(id: Long, type: PinnedType): Boolean =
        libraryDao.checkIfPinned(id, type) != null

    override suspend fun pinData(id: Long, pinnedType: PinReqType) =
        libraryDao.pinData(PinnedEntity(id, pinnedType.toPinnedType()))

    override suspend fun unPinData(id: Long, pinnedType: PinReqType) =
        libraryDao.unData(PinnedEntity(id, pinnedType.toPinnedType()))
}