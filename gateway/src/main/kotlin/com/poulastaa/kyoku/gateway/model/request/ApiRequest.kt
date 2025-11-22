package com.poulastaa.kyoku.gateway.model.request

import com.poulastaa.kyoku.gateway.model.DtoUser

data class ApiRequest<ActualRequest>(
    val user: DtoUser,
    val payload: ActualRequest? = null,
)
