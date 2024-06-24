package com.poulastaa.data.model

import com.poulastaa.data.model.auth.response.GoogleAuthRes

data class GoogleAuthResPayload(
    val response: GoogleAuthRes,
    val userId: Long,
)
