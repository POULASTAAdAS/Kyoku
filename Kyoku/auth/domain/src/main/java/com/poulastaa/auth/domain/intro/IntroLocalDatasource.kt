package com.poulastaa.auth.domain.intro

import com.poulastaa.core.domain.model.DtoUser

interface IntroLocalDatasource {
    suspend fun saveUser(user: DtoUser)
}