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
    val temp = "Whistle Baja 2.0 Heropanti 2";

    println(temp.contains("Heropanti 2"))

//    val temp = "Bulleya (From \"Ae Dil Hai Mushkil\")"
//
//
//    val animal = temp.getAlbum()
//    println(animal)

//    runBlocking {
//        val client = HttpClient()
//        client.checkEmailVerificationStatus()
//    }
}

fun removePart() {
    val url = "https://open.spotify.com/playlist/5BuXPc7nCIh91ClFzIM55O?si=lA3Pks3TQUiOp97MaJPXIg"
    println(
        url.replace("https://open.spotify.com/playlist/", "").replace(Regex("\\?si=.*"), "")
    )
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

fun String.getAlbum(): String {
    val temp = Regex("\"([^\"]+)\"").find(this)

    temp?.let {
        return it.groupValues[1].trim()
    }
    return this.replace(Regex("\\(.*"), "").trim()
}

fun String.removeAlbum(): String =
    this.replace(Regex("\\(.*"), "").trim()
