package com.poulastaa.auth.domain.intro

import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.utils.JWTToken

interface IntroLocalDatasource {
    suspend fun saveUser(user: DtoUser)
    suspend fun saveAccessToken(token: JWTToken)
    suspend fun saveRefreshToken(token: JWTToken)
}