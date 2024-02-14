package com.poulastaa.kyoku.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.Constants.SERVICE_BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.CookieManager
import java.net.URI

fun ViewModel.storeCookieOrAccessToken(data: String, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storeCookieOrAccessToken(data)
    }
}

fun ViewModel.storeProfilePicUri(uri: String, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storeProfilePic(uri)
    }
}

fun ViewModel.storeUsername(username: String, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storeUsername(username)
    }
}

fun ViewModel.storeBDate(date: String, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storeBDate(date)
    }
}


fun ViewModel.storeSignInState(data: SignInStatus, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storeSignedInState(data.name)
    }
}

fun ViewModel.storeRefreshToken(data: String, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storeRefreshToken(data)
    }
}

fun ViewModel.storeAuthType(data: AuthType, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storeAuthType(data.name)
    }
}


fun setCookie(cm: CookieManager, cookie: String) {
    cm.put(
        URI.create(SERVICE_BASE_URL),
        mapOf("Set-Cookie" to listOf(cookie))
    )
}

fun ViewModel.insertIntoPlaylist(db: DatabaseRepositoryImpl, data: List<ResponseSong>) {
    viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            val songId = ArrayList<Long>()
            async {
                data.toListOfSongTable().forEach {
                    songId.add(db.insertSong(it)) // collecting songIds
                }
            }.await()

            val name = generatePlaylistName()

            val playlistId = async {
                db.insertSongIntoPlaylist(toPlaylistTable(name))
            }.await()

            async {
                songId.forEach {
                    db.insertDataIntoPlaylistRelationTable(
                        playlistRelationTable(
                            songId = it,
                            playlistId = playlistId
                        )
                    )
                }
            }.await()
        }
    }
}