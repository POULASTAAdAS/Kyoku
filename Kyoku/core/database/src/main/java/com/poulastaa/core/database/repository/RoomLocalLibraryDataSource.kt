package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.mapper.toArtist
import com.poulastaa.core.database.mapper.toSavedPlaylist
import com.poulastaa.core.domain.library.LocalLibraryDataSource
import com.poulastaa.core.domain.utils.SavedArtist
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalLibraryDataSource @Inject constructor(
    private val commonDao: CommonDao,
    private val libraryDao: LibraryDao,
) : LocalLibraryDataSource {
    override fun getPlaylist(): Flow<SavedPlaylist> = libraryDao.getAllSavedPlaylist().map {
        it.groupBy { result -> result.id }
            .map { groped -> groped.toSavedPlaylist() }
    }

    override fun getArtist(): Flow<SavedArtist> = libraryDao.getAllSavedArtist().map { list ->
        list.map {
            it.toArtist()
        }
    }

    override suspend fun isFavourite(): Boolean = commonDao.isFavourite().isNotEmpty()
}