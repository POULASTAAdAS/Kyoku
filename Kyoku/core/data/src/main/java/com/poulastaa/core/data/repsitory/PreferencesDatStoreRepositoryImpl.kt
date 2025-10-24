package com.poulastaa.core.data.repsitory

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.poulastaa.core.data.mapper.toDtoUser
import com.poulastaa.core.data.mapper.toSerializedUser
import com.poulastaa.core.data.model.SerializedUser
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.repository.PreferencesDatastoreRepository
import com.poulastaa.core.domain.utils.JWTToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PreferencesDatStoreRepositoryImpl @Inject constructor(
    private val ds: DataStore<Preferences>,
    private val gson: Gson,
) : PreferencesDatastoreRepository {
    private object Keys {
        val USER = stringPreferencesKey("local_user")
        val ACCESS = stringPreferencesKey("access_token")
        val REFRESH = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveUser(user: DtoUser) {
        ds.edit { it[Keys.USER] = gson.toJson(user.toSerializedUser()) }
    }

    override suspend fun getUser(): DtoUser? = ds.data.catch { emit(emptyPreferences()) }.map {
        it[Keys.USER]
    }.firstOrNull()?.let {
        gson.fromJson(gson.toJson(it), SerializedUser::class.java)
    }?.toDtoUser()

    override fun getUserFlow(): Flow<DtoUser?> =
        ds.data.catch { emit(emptyPreferences()) }.map {
            it[Keys.USER]
        }.let { flow ->
            flow.map { str ->
                str?.let {
                    gson.fromJson(gson.toJson(it), SerializedUser::class.java).toDtoUser()
                }
            }
        }

    override suspend fun saveAccessToken(token: JWTToken) {
        ds.edit { it[Keys.ACCESS] = token }
    }

    override fun getAccessTokenFlow(): Flow<String?> = ds.data.catch {
        emit(emptyPreferences())
    }.map { it[Keys.ACCESS] }

    override suspend fun getAccessTokenFirst(): String? = ds.data.catch {
        emit(emptyPreferences())
    }.map { it[Keys.ACCESS] }.firstOrNull()

    override suspend fun saveRefreshToken(token: JWTToken) {
        ds.edit { it[Keys.REFRESH] = token }
    }
}