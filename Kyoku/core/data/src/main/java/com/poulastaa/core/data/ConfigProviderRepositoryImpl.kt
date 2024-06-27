package com.poulastaa.core.data

import com.poulastaa.core.domain.ConfigProviderRepository

class ConfigProviderRepositoryImpl : ConfigProviderRepository {
    override fun getClientId(): String = BuildConfig.CLIENT_ID
}