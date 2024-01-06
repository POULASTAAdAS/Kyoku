package com.example.util

import com.example.data.model.VerificationMailApiResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

suspend fun verifyEmailIdWithApi(
    email: String,
): Boolean {
    val client = HttpClient()

    val responseBody = client.get("https://email-checker.p.rapidapi.com/verify/v1?email=${email}") {
        headers {
            append("X-RapidAPI-Key", System.getenv("emailVerifierKey"))
            append("X-RapidAPI-Host", "email-checker.p.rapidapi.com")
        }
    }.bodyAsText()

    client.close()
    return try {
        Json.decodeFromString<VerificationMailApiResponse>(responseBody).status.lowercase() == "valid"
    } catch (e: Exception) {
        false
    }
}