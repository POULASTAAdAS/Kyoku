package com.poulastaa.core.domain.home

import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.model.NewHome
import com.poulastaa.core.domain.model.PrevSavedPlaylist
import com.poulastaa.core.domain.model.PrevSong
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.flow.Flow


interface LocalHomeDatasource {
    suspend fun storeNewHomeResponse(
        dayType: DayType,
        response: NewHome,
    )

    suspend fun isNewUser(): Boolean

    suspend fun loadHomeStaticData(): HomeData
    fun loadSavedPlaylist(): Flow<SavedPlaylist>
}