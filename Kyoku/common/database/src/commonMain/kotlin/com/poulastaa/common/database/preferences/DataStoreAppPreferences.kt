package com.poulastaa.common.database.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.poulastaa.common.domain.model.DtoSavedScreen
import com.poulastaa.common.domain.model.DtoTokens
import com.poulastaa.common.domain.preferences.AppPreferences
import kotlinx.coroutines.flow.first

private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
private val LAST_SETUP_SCREEN_KEY = stringPreferencesKey("last_setup_screen")

class DataStoreAppPreferences(
    private val dataStore: DataStore<Preferences>,
) : AppPreferences {
    override suspend fun saveTokens(tokens: DtoTokens) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = tokens.accessToken
            preferences[REFRESH_TOKEN_KEY] = tokens.refreshToken
        }
    }

    override suspend fun getTokens(): DtoTokens? {
        val preferences = dataStore.data.first()
        val accessToken = preferences[ACCESS_TOKEN_KEY]?.takeIf(String::isNotBlank) ?: return null
        val refreshToken = preferences[REFRESH_TOKEN_KEY]?.takeIf(String::isNotBlank) ?: return null

        return DtoTokens(accessToken = accessToken, refreshToken = refreshToken)
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    override suspend fun saveLastSetupScreen(screen: DtoSavedScreen) {
        dataStore.edit { preferences ->
            preferences[LAST_SETUP_SCREEN_KEY] = screen.name
        }
    }

    override suspend fun getLastSetupScreen(): DtoSavedScreen? {
        val value = dataStore.data.first()[LAST_SETUP_SCREEN_KEY] ?: return null
        return DtoSavedScreen.entries.firstOrNull { it.name == value }
    }
}
