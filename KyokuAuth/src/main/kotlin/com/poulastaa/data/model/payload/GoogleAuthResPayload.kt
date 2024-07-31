package com.poulastaa.data.model.payload

import com.poulastaa.data.model.auth.response.UserAuthRes

data class GoogleAuthResPayload(
    val response: UserAuthRes,
    val userId: Long,
)
