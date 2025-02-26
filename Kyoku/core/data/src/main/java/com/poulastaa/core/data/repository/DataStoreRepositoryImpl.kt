package com.poulastaa.core.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.poulastaa.core.data.model.UserSerializable
import com.poulastaa.core.data.toUser
import com.poulastaa.core.data.toUserSerializable
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.domain.repository.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
) : DatastoreRepository {
    private object PreferencesKeys {
        val SIGN_IN_STATE = stringPreferencesKey(name = "sign_in_state")
        val TOKEN_OR_COOKIE = stringPreferencesKey(name = "token_or_cookie")
        val REFRESH_TOKEN = stringPreferencesKey(name = "refresh_token")
        val LOCAL_USER = stringPreferencesKey(name = "local_user")
        val LIBRARY_VIEW_TYPE = booleanPreferencesKey(name = "library_view_type")
        val APP_THEME = booleanPreferencesKey(name = "app_theme")
    }

    override suspend fun storeThem(them: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.APP_THEME] = them
        }
    }

    override fun readThem(): Flow<Boolean> = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        Log.d("called", "called")

        it[PreferencesKeys.APP_THEME] ?: true // first dark theme
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

    override suspend fun storeLocalUser(user: DtoUser) {
        val jsonString = gson.toJson(user.toUserSerializable())

        dataStore.edit {
            it[PreferencesKeys.LOCAL_USER] = jsonString
        }
    }

    override suspend fun readLocalUser(): DtoUser {
        val pref = dataStore.data.catch {
            emit(emptyPreferences())
        }.first()

        val response = pref[PreferencesKeys.LOCAL_USER]?.let {
            gson.fromJson(it, UserSerializable::class.java)
        }

        return response?.toUser() ?: DtoUser()
    }

    override suspend fun updateBDate(bDate: String) {
        val pref = dataStore.data.catch {
            emit(emptyPreferences())
        }.first()

        val user = pref[PreferencesKeys.LOCAL_USER]?.let {
            gson.fromJson(it, UserSerializable::class.java)
        } ?: return

        val updatedUser = user.copy(bDate = bDate)

        dataStore.edit {
            it[PreferencesKeys.LOCAL_USER] = gson.toJson(updatedUser)
        }
    }

    override suspend fun storeLibraryViewType(isGrid: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.LIBRARY_VIEW_TYPE] = isGrid
        }
    }

    override fun readLibraryViewType(): Flow<Boolean> = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        it[PreferencesKeys.LIBRARY_VIEW_TYPE] ?: true // first grid view
    }

    override suspend fun logOut() {
        dataStore.edit {
            it.clear()
        }
    }
}