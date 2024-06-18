package com.poulastaa.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.StartScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreRepository {
    private object PreferencesKeys {
        val SIGN_IN_STATE = stringPreferencesKey(name = "sign_in_state")
        val TOKEN_OR_COOKIE = stringPreferencesKey(name = "token_or_cookie")
        val LOCAL_USER = stringPreferencesKey(name = "local_user")
    }

    override suspend fun storeSignInState(state: StartScreen) {
        when (state) {
            StartScreen.INTRO -> StartScreen.INTRO.name
            StartScreen.GET_SPOTIFY_PLAYLIST -> StartScreen.GET_SPOTIFY_PLAYLIST.name
            StartScreen.SET_B_DATE -> StartScreen.SET_B_DATE.name
            StartScreen.PIC_GENRE -> StartScreen.PIC_GENRE.name
            StartScreen.PIC_ARTIST -> StartScreen.PIC_ARTIST.name
            StartScreen.HOME -> StartScreen.HOME.name
        }.let { value ->
            dataStore.edit {
                it[PreferencesKeys.SIGN_IN_STATE] = value
            }
        }
    }

    override suspend fun readSignInState(): StartScreen = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        val state = it[PreferencesKeys.SIGN_IN_STATE] ?: StartScreen.INTRO.name
        StartScreen.valueOf(state)
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
}