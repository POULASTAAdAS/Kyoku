package com.poulastaa.core.network.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.network.domain.model.DtoReqParam
import com.poulastaa.core.network.domain.model.Endpoints
import java.lang.reflect.Type

interface ApiRepository {
    enum class Method {
        GET,
        POST,
        PUT
    }

    suspend fun <Req : Any, Res : Any> authReq(
        route: Endpoints,
        method: Method,
        type: Type,
        body: Req? = null,
        params: List<DtoReqParam> = emptyList(),
    ): Result<Res, DataError.Network>

//    suspend fun <Req : Any, Res : Any> apiReq(
//        route: Endpoints,
//        method: Method,
//        type: Type,
//        body: Req? = null,
//        params: List<DtoReqParam> = emptyList(),
//    ): Result<Res, DataError.Network>
}