package com.example.data.model

import com.example.routes.auth.common.EmailLoginStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginResponse( // todo add other fields
    val status: EmailLoginStatus
)
