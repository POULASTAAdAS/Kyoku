package com.poulastaa.play.data

import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.LibraryDataType
import com.poulastaa.core.domain.library.LibraryRepository
import com.poulastaa.core.domain.library.LocalLibraryDataSource
import com.poulastaa.core.domain.library.RemoteLibraryDataSource
import com.poulastaa.core.domain.model.PinnedData
import com.poulastaa.core.domain.model.PinnedType
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.SavedAlbum
import com.poulastaa.core.domain.utils.SavedArtist
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstLibraryRepository @Inject constructor(
    private val local: LocalLibraryDataSource,
    private val remote: RemoteLibraryDataSource,
    private val application: CoroutineScope,
    private val ds: DataStoreRepository
) : LibraryRepository {
    override fun getPlaylist(): Flow<SavedPlaylist> = local.getPlaylist()

    override fun getAlbum(): Flow<SavedAlbum> = local.getAlbum()

    override fun getArtist(): Flow<SavedArtist> = local.getArtist()

    override suspend fun isFavourite(): Boolean = local.isFavourite()

    override suspend fun getPinnedData(): Flow<List<PinnedData>> = local.getPinnedData()

    override suspend fun checkIfPinned(id: Long, type: PinnedType): Boolean =
        local.checkIfPinned(id, type)

    override suspend fun pinData(id: Long, type: LibraryDataType): EmptyResult<DataError.Network> {
        val result = remote.pinData(id, type)
        if (result is Result.Success) {
            application.async {
                if (type == LibraryDataType.FAVOURITE) ds.updateFevPinState(true)
                else local.pinData(id, type)
            }.await()
        }

        return result
    }

    override suspend fun unPinData(
        id: Long,
        type: LibraryDataType
    ): EmptyResult<DataError.Network> {
        val result = remote.unPinData(id, type)
        if (result is Result.Success) {
            application.async {
                if (type == LibraryDataType.FAVOURITE) ds.updateFevPinState(false)
                else local.unPinData(id, type)
            }.await()
        }

        return result
    }

    override suspend fun deleteSavedData(
        id: Long,
        type: LibraryDataType
    ): EmptyResult<DataError.Network> {
        val result = remote.deleteSavedData(id, type)
        if (result is Result.Success) application.async {
            val unPin = async {
                if (type == LibraryDataType.FAVOURITE) ds.updateFevPinState(false)
                else local.unPinData(id, type)
            }
            val delete = async { local.deleteSavedData(id, type) }

            unPin.await()
            delete.await()
        }.await()

        return result
    }
}