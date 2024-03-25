package com.poulastaa.kyoku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_AUTH_TYPE_KEY
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_B_DATE_KEY
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_EMAIL_KEY
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_JWT_ACCESS_TOKEN_OR_SESSION_COOKIE_KEY
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_JWT_REFRESH_TOKEN_KEY
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_PASSWORD_KEY
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_PROFILE_PIC_KEY
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_SIGNED_IN_KEY
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_USERNAME_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreOperationImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreOperation {
    private object PreferencesKey {
        val signedInKey = stringPreferencesKey(name = PREFERENCES_SIGNED_IN_KEY)
        val authTypeKey = stringPreferencesKey(name = PREFERENCES_AUTH_TYPE_KEY)

        val usernameKey = stringPreferencesKey(name = PREFERENCES_USERNAME_KEY)
        val profilePicUrlKey = stringPreferencesKey(name = PREFERENCES_PROFILE_PIC_KEY)

        val emailKey = stringPreferencesKey(name = PREFERENCES_EMAIL_KEY)
        val passwordKey = stringPreferencesKey(name = PREFERENCES_PASSWORD_KEY)


        val accessTokenOrCookieKey =
            stringPreferencesKey(name = PREFERENCES_JWT_ACCESS_TOKEN_OR_SESSION_COOKIE_KEY)
        val refreshTokenKey = stringPreferencesKey(name = PREFERENCES_JWT_REFRESH_TOKEN_KEY)

        val bDateKey = stringPreferencesKey(name = PREFERENCES_B_DATE_KEY)

        val librarySortType = booleanPreferencesKey(name = "")
    }

    override suspend fun storeSignedInState(signedInState: String) {
        dataStore.edit {
            it[PreferencesKey.signedInKey] = signedInState
        }
    }

    override fun readSignedInState(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val signedInState = it[PreferencesKey.signedInKey] ?: SignInStatus.AUTH.name
            signedInState
        }

    override suspend fun storeUsername(username: String) {
        dataStore.edit {
            it[PreferencesKey.usernameKey] = username
        }
    }

    override fun readUsername(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val username = it[PreferencesKey.usernameKey] ?: "User"
            username
        }


    override suspend fun storeProfilePic(uir: String) {
        dataStore.edit {
            it[PreferencesKey.profilePicUrlKey] = uir
        }
    }

    override fun readProfilePic(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val uri = it[PreferencesKey.profilePicUrlKey] ?: ""
            uri
        }

    override suspend fun storeEmail(email: String) {
        dataStore.edit {
            it[PreferencesKey.emailKey] = email
        }
    }

    override fun readEmail(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val email = it[PreferencesKey.emailKey] ?: ""
            email
        }

    override suspend fun storePassword(password: String) {
        dataStore.edit {
            it[PreferencesKey.passwordKey] = password
        }
    }

    override fun readPassword(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val password = it[PreferencesKey.passwordKey] ?: ""
            password
        }

    override suspend fun storeCookieOrAccessToken(data: String) {
        dataStore.edit {
            it[PreferencesKey.accessTokenOrCookieKey] = data
        }
    }


    override fun readTokenOrCookie(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val tokenOrCookie = it[PreferencesKey.accessTokenOrCookieKey] ?: ""
            tokenOrCookie
        }

    override suspend fun storeRefreshToken(data: String) {
        dataStore.edit {
            it[PreferencesKey.refreshTokenKey] = data
        }
    }

    override fun readRefreshToken(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val tokenOrCookie = it[PreferencesKey.refreshTokenKey] ?: ""
            tokenOrCookie
        }

    override suspend fun storeAuthType(data: String) {
        dataStore.edit {
            it[PreferencesKey.authTypeKey] = data
        }
    }

    override fun readAuthType(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val authType = it[PreferencesKey.authTypeKey] ?: AuthType.UN_AUTH.name
            authType
        }

    override suspend fun storeBDate(date: String) {
        dataStore.edit {
            it[PreferencesKey.bDateKey] = date
        }
    }

    override fun readBDate(): Flow<String> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val bDate = it[PreferencesKey.bDateKey] ?: ""
            bDate
        }


    override suspend fun storeLibraryDataSortType(sortType: Boolean) {
        dataStore.edit {
            it[PreferencesKey.librarySortType] = sortType
        }
    }

    override fun readLibraryDataSortType(): Flow<Boolean> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val sortType = it[PreferencesKey.librarySortType] ?: true
            sortType
        }
}