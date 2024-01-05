package com.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.apache.commons.validator.routines.EmailValidator

fun main() {
    runBlocking {
        val client = HttpClient()
        client.checkEmailVerificationStatus()
    }
}

suspend fun HttpClient.checkEmailVerificationStatus() {
    val response = get("https://email-checker.p.rapidapi.com/verify/v1?email=example@gmail.com") {
        headers {
            append("X-RapidAPI-Key", System.getenv("emailVerifierKey"))
            append("X-RapidAPI-Host", "email-checker.p.rapidapi.com")
        }
    }

    val responseBody = response.bodyAsText()

    val apiResponse = Json.decodeFromString<ApiResponse>(responseBody)

    println(apiResponse.status)
}