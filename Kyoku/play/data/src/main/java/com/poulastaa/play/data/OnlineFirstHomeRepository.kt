package com.poulastaa.play.data

import com.poulastaa.core.domain.home.HomeRepository
import com.poulastaa.core.domain.home.LocalHomeDatasource
import com.poulastaa.core.domain.home.RemoteHomeDatasource
import com.poulastaa.core.domain.home.SavedPlaylist
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstHomeRepository @Inject constructor(
    private val local: LocalHomeDatasource,
    private val remote: RemoteHomeDatasource,
    private val application: CoroutineScope,
) : HomeRepository {
    override suspend fun storeNewHomeResponse(dayType: DayType): EmptyResult<DataError.Network> {
        val result = remote.getNewHomeResponse(dayType)

        if (result is Result.Error) return result.asEmptyDataResult()

        application.async {
            result.map {
                local.storeNewHomeResponse(
                    dayType = dayType,
                    response = it
                )
            }
        }.await()

        return Result.Success(Unit)
    }

    override suspend fun isNewUser(): Boolean = local.isNewUser()

    override suspend fun loadHomeData(): HomeData = application.async {
        local.loadHomeStaticData()
    }.await()

    override fun loadSavedPlaylist(): Flow<SavedPlaylist> = local.loadSavedPlaylist()
}