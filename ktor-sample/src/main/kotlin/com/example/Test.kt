package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class DefaultAuthModel(
    val authType: String
)

@Serializable
data class EmailLoginReq(
    val type: String,
    val authType: String,
    val email: String,
    val password: String
)

fun main() {
    val data = EmailLoginReq(
        type = "com.example.EmailLoginReq",
        authType = "AUTH_TYPE_EMAIL_LOG_IN",
        email = "example.email",
        password = "examplePassword"
    )
    println(Json.encodeToString(data)) // Serializing data of compile-time type Project
}