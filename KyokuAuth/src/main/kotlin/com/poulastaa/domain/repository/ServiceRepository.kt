package com.poulastaa.domain.repository

import com.poulastaa.data.model.auth.req.EmailLogInReq
import com.poulastaa.data.model.auth.req.EmailSignUpReq
import com.poulastaa.data.model.auth.res.Payload
import com.poulastaa.data.model.auth.response.EmailAuthRes
import com.poulastaa.data.model.auth.response.GoogleAuthRes

typealias UserId = Long

interface ServiceRepository {
    suspend fun createEmailUser(
        req: EmailSignUpReq,
    ): EmailAuthRes

    suspend fun loginEmailUser(
        req: EmailLogInReq,
    ): EmailAuthRes

    suspend fun googleAuth(
        payload: Payload,
        countryCode: String,
    ): Pair<GoogleAuthRes, UserId>
}