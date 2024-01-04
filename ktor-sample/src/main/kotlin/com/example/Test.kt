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
    val email: String,
    val password: String
) : DefaultAuthModel(
    authType = "AUTH_TYPE_EMAIL_LOG_IN"
)

fun main() {
    val data: DefaultAuthModel = EmailLoginReq("example.email", "examplePassword")
    println(Json.encodeToString(data)) // Serializing data of compile-time type Project
}