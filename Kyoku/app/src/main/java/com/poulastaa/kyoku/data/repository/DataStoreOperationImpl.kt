package com.poulastaa.kyoku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.Constants.PREFERENCES_SIGNED_IN_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreOperationImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreOperation {
    private object PreferencesKey {
        val signedInKey = booleanPreferencesKey(name = PREFERENCES_SIGNED_IN_KEY)
    }

    override suspend fun storeSignedInState(signedInState: Boolean) {
        dataStore.edit {
            it[PreferencesKey.signedInKey] = signedInState
        }
    }

    override fun readSignedInState(): Flow<Boolean> = dataStore
        .data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val signedInState = it[PreferencesKey.signedInKey] ?: false
            signedInState
        }
}