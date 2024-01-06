package com.example.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginResponse( // todo add other fields
    val status: EmailLoginStatus,
    val userName: String = "",
    val profilePhoto: String? = null, // todo change
    val data: List<String> = emptyList(), // todo change
    val token: String = ""
)
