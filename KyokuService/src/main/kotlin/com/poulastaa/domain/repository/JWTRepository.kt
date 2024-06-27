package com.poulastaa.domain.repository

interface JWTRepository {
    fun verifyJWTToken(token: String, claim: String): String?

    fun getIssuer(): String
    fun getRealm(): String
}