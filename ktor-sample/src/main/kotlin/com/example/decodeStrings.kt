package com.example

import com.upokecenter.cbor.CBORObject
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.spec.*
import java.util.*

fun main() {
    val publicKey =
        "wuyXvmrde9hyjZBmNL5wDKSPRH_4Ivc6JS4qHPeQJVBdAAAAAOqbjWZNAR0hPOS2tIy1ddQAEBcMz-hOC1bFpP7bSfjydo-lAQIDJiABIVgg1bzte_gaJmcufiMhlpEaWkT1JJfOvMK4O0zPo4uAkl0iWCBr61JoNw5BDXSa7yVyG4SJJmVOVQRq9iS62M0a-3qODg=="

    val another =
        "wuyXvmrde9hyjZBmNL5wDKSPRH_4Ivc6JS4qHPeQJVBdAAAAAOqbjWZNAR0hPOS2tIy1ddQAEBcMz-hOC1bFpP7bSfjydo-lAQIDJiABIVgg1bzte_gaJmcufiMhlpEaWkT1JJfOvMK4O0zPo4uAkl0iWCBr61JoNw5BDXSa7yVyG4SJJmVOVQRq9iS62M0a-3qODg=="

    val byteArray = publicKey.b64Decode()

    temp(byteArray)

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

    // decode PublicKey
    val previousByteArray = encodedPublicKey.b64Decode()
    println("-----------------------------------------")
    println(byteArray)
    println(previousByteArray)
    println(byteArray.contentEquals(previousByteArray))
}

private fun temp(byteArray: ByteArray) {
    val publicKey = byteArray.joinToString("") { "%02x".format(it) }
    println(publicKey)

    val hash = byteArray.copyOfRange(0, 32)
    val publicKeyByteArray = byteArray.copyOfRange(32, 64)

    //    println("Hash: ${hash.joinToString("") { "%02x".format(it) }}")
//    println("Public Key: ${publicKeyByteArray.joinToString("") { "%02x".format(it) }}")

    val ecPoint = ECPoint(BigInteger(1, hash), BigInteger(1, publicKeyByteArray))

    val ecParameterSpec = EllipticCurves.secp256k1().curve

    val ecKeySpec = ECPublicKeySpec(ecPoint, ecParameterSpec)

    val keyFactory = KeyFactory.getInstance("EC")
    val ecPublicKey = keyFactory.generatePublic(ecKeySpec)

    println("ecPublicKey: ${ecPublicKey}")
}


class EllipticCurves {
    companion object {
        fun secp256k1(): ECParams {
            val p = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16)
            val a = BigInteger.ZERO
            val b = BigInteger("7")
            val n = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16)
            val gX = BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16)
            val gY = BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16)

            val curveParams = ECParameterSpec(EllipticCurve(ECFieldFp(p), a, b), ECPoint(gX, gY), n, 1)
            return ECParams(curveParams)
        }
    }

    data class ECParams(val curve: ECParameterSpec)
}


fun String.b64Decode(): ByteArray {
    return Base64.getUrlDecoder().decode(this)
}

fun ByteArray.b64Encode(): String {
    return Base64.getUrlEncoder().encodeToString(this)
}