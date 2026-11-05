package com.poulastaa.common.domain.preferences

import com.poulastaa.common.domain.model.DtoTokens

/**
 * App-wide key-value storage. Add new preference reads/writes here instead of creating a separate
 * storage per feature.
 */
interface AppPreferences {
    suspend fun saveTokens(tokens: DtoTokens)
    suspend fun getTokens(): DtoTokens?
    suspend fun clearTokens()
}
