package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.ProfileDao
import com.poulastaa.core.domain.repository.LocalProfileDatasource
import javax.inject.Inject

internal class RoomLocalProfileDatasource @Inject constructor(
    private val dao: ProfileDao,
) : LocalProfileDatasource {
    override suspend fun getSavedData(): List<Pair<Int, Int>> {
        return listOf(
            1 to dao.countSavedPlaylist(), // i am to lazy to create another enum class deal with it :)
            2 to dao.countSavedAlbum(),
            3 to dao.countSavedArtist(),
            4 to dao.countFavouriteSongs()
        )
    }
}