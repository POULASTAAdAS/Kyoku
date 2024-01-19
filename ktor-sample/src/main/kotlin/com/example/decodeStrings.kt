package com.example

import com.upokecenter.cbor.CBORObject
import java.nio.charset.StandardCharsets
import java.util.*

fun main() {

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

    println(attestationObject)

    val attStmt = attestationObject["authData"].toString().substring(2).removeSuffix("'")

    val byteArray = attStmt.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

    println()
    println(byteArray.b64Encode())
    println()

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