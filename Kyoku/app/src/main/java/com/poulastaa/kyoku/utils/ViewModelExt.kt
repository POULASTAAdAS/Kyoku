package com.poulastaa.kyoku.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

fun ViewModel.storeEmail(email: String, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storeEmail(email)
    }
}

fun ViewModel.storePassword(password: String, ds: DataStoreOperation) {
    viewModelScope.launch(Dispatchers.IO) {
        ds.storePassword(password)
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

fun ViewModel.insertIntoPlaylist(
    db: DatabaseRepositoryImpl,
    data: List<ResponseSong>,
    playlistName: String
) {
    viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            val songIdList = async {
                data.toListOfSongTable().map {
                    db.insertSong(it)
                }
            }.await()


            val playlistId = async {
                db.insertPlaylist(playlistName)
            }.await()

            async {
                songIdList.forEach {
                    db.insertSongPlaylistRelation(
                        data = playlistRelationTable(
                            songId = it,
                            playlistId = playlistId
                        )
                    )
                }
            }.await()
        }
    }
}