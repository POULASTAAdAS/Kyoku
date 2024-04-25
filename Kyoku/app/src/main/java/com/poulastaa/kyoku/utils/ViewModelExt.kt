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

suspend fun storeData(
    context: Context,
    tokenOrCookie: String,
    response: HomeResponse,
    db: DatabaseRepositoryImpl
) {
    withContext(Dispatchers.IO) {
        db.setValues(
            context,
            tokenOrCookie
        )

        val artistMixDef =
            async { db.insertIntoFevArtistMixPrev(list = response.fevArtistsMixPreview) }

        val albumPrevDef =
            async { db.insertIntoAlbumPrev(list = response.albumPreview.listOfPreviewAlbum) }

        val artist = async { db.insertResponseArtistPrev(list = response.artistsPreview) }

        db.insertDailyMixPrev(data = response.dailyMixPreview)

        val playlist = async { db.insertIntoPlaylist(list = response.playlist) }

        val fav = async { db.insertIntoFavourite(list = response.favourites.listOfSongs) }

        val album = async { db.insertIntoAlbum(list = response.albums) }

        val history = async { db.insertIntoRecentlyPlayedPrev(list = response.historyPreview) }

        artistMixDef.await()
        albumPrevDef.await()
        artist.await()
        fav.await()
        album.await()
        history.await()
        playlist.await()

        db.insertIntoPinned(list = response.pinned)
    }
}

























