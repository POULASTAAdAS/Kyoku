package com.example

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val email: String,
    val user: String,
    val domain: String,
    val status: String,
    val reason: String,
    val disposable: Boolean
)
