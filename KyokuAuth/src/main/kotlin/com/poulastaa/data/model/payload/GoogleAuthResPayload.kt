package com.poulastaa.data.model.payload

import com.poulastaa.data.model.auth.response.GoogleAuthRes

data class GoogleAuthResPayload(
    val response: GoogleAuthRes,
    val userId: Long,
)
