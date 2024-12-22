package com.poulastaa.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.poulastaa.core.data.model.UserSerializable
import com.poulastaa.core.data.toUser
import com.poulastaa.core.data.toUserSerializable
import com.poulastaa.core.domain.DatastoreRepository
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.domain.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DatastoreRepository {
    private object PreferencesKeys {
        val SIGN_IN_STATE = stringPreferencesKey(name = "sign_in_state")
        val TOKEN_OR_COOKIE = stringPreferencesKey(name = "token_or_cookie")
        val REFRESH_TOKEN = stringPreferencesKey(name = "refresh_token")
        val LOCAL_USER = stringPreferencesKey(name = "local_user")
        val LIBRARY_VIEW_TYPE = booleanPreferencesKey(name = "library_view_type")
    }

    override suspend fun storeSignInState(state: SavedScreen) {
        dataStore.edit {
            it[PreferencesKeys.SIGN_IN_STATE] = state.name
        }
    }

    override suspend fun readSignInState(): SavedScreen = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        val state = it[PreferencesKeys.SIGN_IN_STATE] ?: SavedScreen.INTRO.name
        SavedScreen.valueOf(state)
    }.first()

    override suspend fun storeTokenOrCookie(data: String) {
        dataStore.edit {
            it[PreferencesKeys.TOKEN_OR_COOKIE] = data
        }
    }

    override fun readTokenOrCookie(): Flow<String> = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        it[PreferencesKeys.TOKEN_OR_COOKIE] ?: ""
    }

    override suspend fun storeRefreshToken(data: String) {
        dataStore.edit {
            it[PreferencesKeys.REFRESH_TOKEN] = data
        }
    }

    override suspend fun readRefreshToken(): String = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        it[PreferencesKeys.REFRESH_TOKEN] ?: ""
    }.first()

    override suspend fun storeLocalUser(user: UserDto) {
        val jsonString = Json.encodeToString(user.toUserSerializable())

        dataStore.edit {
            it[PreferencesKeys.LOCAL_USER] = jsonString
        }
    }

    override suspend fun readLocalUser(): UserDto {
        val pref = dataStore.data.catch {
            emit(emptyPreferences())
        }.first()

        val response = pref[PreferencesKeys.LOCAL_USER]?.let {
            Json.decodeFromString<UserSerializable>(it)
        }

        return response?.toUser() ?: UserDto()
    }

    override suspend fun storeLibraryViewType(isGrid: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.LIBRARY_VIEW_TYPE] = isGrid
        }
    }

    override suspend fun readLibraryViewType(): Boolean = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        it[PreferencesKeys.LIBRARY_VIEW_TYPE] ?: true // first grid view
    }.first()

    override suspend fun logOut() {
        dataStore.edit {
            it.clear()
        }
    }
}