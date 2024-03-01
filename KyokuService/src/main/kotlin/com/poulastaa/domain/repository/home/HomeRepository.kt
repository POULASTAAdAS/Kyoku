package com.poulastaa.domain.repository.home

import com.poulastaa.data.model.home.HomeReq
import com.poulastaa.data.model.home.HomeResponse
import com.poulastaa.data.model.utils.UserTypeHelper

interface HomeRepository {
    suspend fun generateHomeResponse(req: HomeReq, helper: UserTypeHelper): HomeResponse
}