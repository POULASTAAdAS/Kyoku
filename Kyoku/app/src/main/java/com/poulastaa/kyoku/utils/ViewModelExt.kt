package com.poulastaa.kyoku.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponse
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

fun ViewModel.storeData(
    context: Context,
    tokenOrCookie: String,
    response: HomeResponse,
    db: DatabaseRepositoryImpl
) {
    viewModelScope.launch(Dispatchers.IO) {
        db.setValues(
            context,
            tokenOrCookie
        )

        db.insertIntoFevArtistMixPrev(list = response.fevArtistsMixPreview)
        db.insertIntoAlbumPrev(list = response.albumPreview.listOfPreviewAlbum)
        db.insertResponseArtistPrev(list = response.artistsPreview)

        db.insertDailyMixPrev(data = response.dailyMixPreview)

        db.insertIntoPlaylistHome(list = response.playlist)
        db.insertIntoFavourite(list = response.favourites.listOfSongs)
        db.insertIntoAlbum(list = response.albums)
        db.insertIntoRecentlyPlayedPrev(list = response.historyPreview)
    }
}

























