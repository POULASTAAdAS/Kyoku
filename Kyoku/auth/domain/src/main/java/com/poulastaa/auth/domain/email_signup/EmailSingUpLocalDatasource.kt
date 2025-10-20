package com.poulastaa.auth.domain.email_signup

import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.utils.JWTToken

interface EmailSingUpLocalDatasource {
    suspend fun saveUser(user: DtoUser)
    suspend fun saveAccessToken(token: JWTToken)
    suspend fun saveRefreshToken(token: JWTToken)
}