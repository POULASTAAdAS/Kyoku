package com.poulastaa.core.domain

interface ConfigProviderRepository {
    fun getClientId(): String
}