package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.domain.repository.LocalExploreAlbumDatasource
import javax.inject.Inject

internal class RoomLocalExploreAlbumDatasource @Inject constructor(
    private val root: RootDao,
) : LocalExploreAlbumDatasource {
}