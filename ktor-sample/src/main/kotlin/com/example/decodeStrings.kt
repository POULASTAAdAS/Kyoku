package com.example

import com.upokecenter.cbor.CBORObject
import java.nio.charset.StandardCharsets
import java.util.*

fun main() {
    val clientDataJson =
        "eyJ0eXBlIjoid2ViYXV0aG4uY3JlYXRlIiwiY2hhbGxlbmdlIjoiMVhHQXk2Y19zRmxOSUtZLUZxZ1IyRlkwMHB1N3AyZVhnUTM3bTJmcjNxbVZORUhIU0pXcTBUOVdzaVNacU1OTXZhT1VCcmJUTDhqbzdGak5MMGNXZ2ciLCJvcmlnaW4iOiJhbmRyb2lkOmFway1rZXktaGFzaDoxaFBoNVFhbE9MZ3ltZ1NWdnh2NVZVSnhwS1pQTkZBcHFzX1JIS3lSYldBIiwiYW5kcm9pZFBhY2thZ2VOYW1lIjoiY29tLnBvdWxhc3RhYS5wYXNzZWt5YXBwIn0"

    val dataString = clientDataJson.decodeBase64()

    println(dataString)
//    attestationObject()
}


private fun clientDataJson() {
    val clientDataJSON = System.getenv("clientDataJSON")

    val dataString = clientDataJSON.decodeBase64()

    println(dataString)
}


private fun String.decodeBase64(): String {
    val decodedBytes = Base64.getUrlDecoder().decode(this)
    return String(decodedBytes, StandardCharsets.UTF_8)
}


private fun attestationObject() {
    val attestationByteArray = System.getenv("attestationObject").b64Decode()

    val attestationObject = CBORObject.DecodeFromBytes(attestationByteArray)

    val attStmt = attestationObject["authData"].toString().substring(2).removeSuffix("'")
    val byteArray = attStmt.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

    // encode PublicKey
    val encodedPublicKey = byteArray.b64Encode()

    temp(byteArray)
}

private fun temp(byteArray: ByteArray) {
    val publicKey = byteArray.joinToString("") { "%02x".format(it) }
    println(publicKey)

    val hash = byteArray.copyOfRange(0, 32)
    val publicKeyByteArray = byteArray.copyOfRange(32, 64)

    println("Hash: ${hash.joinToString("") { "%02x".format(it) }}")
    println("Public Key: ${publicKeyByteArray.joinToString("") { "%02x".format(it) }}")
}


fun String.b64Decode(): ByteArray {
    return Base64.getUrlDecoder().decode(this)
}

fun ByteArray.b64Encode(): String {
    return Base64.getUrlEncoder().encodeToString(this)
}