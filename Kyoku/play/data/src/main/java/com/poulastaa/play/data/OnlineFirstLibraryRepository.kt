package com.poulastaa.play.data

import com.poulastaa.core.domain.library.LibraryRepository
import com.poulastaa.core.domain.library.LocalLibraryDataSource
import com.poulastaa.core.domain.utils.SavedAlbum
import com.poulastaa.core.domain.utils.SavedArtist
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstLibraryRepository @Inject constructor(
    private val local: LocalLibraryDataSource,
    private val application: CoroutineScope,
) : LibraryRepository {
    override fun getPlaylist(): Flow<SavedPlaylist> = local.getPlaylist()

    override fun getAlbum(): Flow<SavedAlbum> = local.getAlbum()

    override fun getArtist(): Flow<SavedArtist> = local.getArtist()

    override suspend fun isFavourite(): Boolean = local.isFavourite()
}