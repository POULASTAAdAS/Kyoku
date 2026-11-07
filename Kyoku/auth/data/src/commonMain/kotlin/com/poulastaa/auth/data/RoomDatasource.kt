package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthLocalDatasource
import com.poulastaa.common.database.dao.AuthDao
import com.poulastaa.common.database.entity.UserEntity
import com.poulastaa.common.domain.model.DtoSetupScreen
import com.poulastaa.common.domain.model.DtoTokens
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.domain.preferences.AppPreferences
import com.poulastaa.common.domain.result.DataError
import com.poulastaa.common.domain.result.EmptyResponse
import com.poulastaa.common.domain.result.safeDbCall
import org.koin.core.annotation.Single

@Single(binds = [AuthLocalDatasource::class])
class RoomDatasource(
    private val dao: AuthDao,
    private val appPreferences: AppPreferences,
) : AuthLocalDatasource {
    override suspend fun saveUser(user: DtoUser): EmptyResponse<DataError> = safeDbCall {
        dao.upsertUser(
            UserEntity(
                userId = user.userId,
                username = user.username,
                email = user.email,
                profilePictureUrl = user.profilePictureUrl,
            )
        )
    }

    override suspend fun saveTokens(tokens: DtoTokens): EmptyResponse<DataError> = safeDbCall {
        appPreferences.saveTokens(tokens)
    }

    override suspend fun saveSetupScreen(screen: DtoSetupScreen): EmptyResponse<DataError> = safeDbCall {
        appPreferences.saveLastSetupScreen(screen)
    }
}