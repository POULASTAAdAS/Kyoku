package com.poulastaa.kyoku.utils

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.gson.JsonParser
import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInReq
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpReq
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.api.service.ResponseArtist
import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import com.poulastaa.kyoku.data.model.api.service.home.AlbumPreview
import com.poulastaa.kyoku.data.model.api.service.home.FevArtistsMixPreview
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.StoreArtistReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.SuggestArtistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.UiArtist
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.StoreGenreReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.UiGenre
import com.poulastaa.kyoku.data.model.database.AlbumPrevResult
import com.poulastaa.kyoku.data.model.database.ArtistPrevResult
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
import com.poulastaa.kyoku.data.model.database.SongInfo
import com.poulastaa.kyoku.data.model.database.table.AlbumPrevTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPrevTable
import com.poulastaa.kyoku.data.model.database.table.FevArtistsMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongTable
import com.poulastaa.kyoku.data.model.screens.auth.email.login.EmailLogInState
import com.poulastaa.kyoku.data.model.screens.auth.email.signup.EmailSignUpState
import com.poulastaa.kyoku.data.model.screens.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.data.model.screens.home.HomeUiFevArtistMix
import com.poulastaa.kyoku.data.model.screens.home.HomeUiSongPrev
import com.poulastaa.kyoku.data.model.ui.UiPlaylist
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_EMAIL_LOG_IN
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_EMAIL_SIGN_UP
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_GOOGLE
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_PASSKEY
import com.poulastaa.kyoku.utils.Constants.TYPE_EMAIL_LOG_IN_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_EMAIL_SIGN_UP_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_GOOGLE_AUTH_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_PASSKEY_AUTH_REQ
import kotlinx.coroutines.runBlocking

fun RootAuthScreenState.toPasskeyAuthRequest() = PasskeyAuthReq(
    type = TYPE_PASSKEY_AUTH_REQ,
    authType = AUTH_TYPE_PASSKEY,
    email = this.passkeyEmail,
    displayName = this.passkeyEmail.removeSuffix("@gmail.com")
)

fun String.toPasskeyJson(): PasskeyJson {
    val json = JsonParser().parse(this).asJsonObject

    val type = json["type"].asString
    json.remove("type")

    val token = json["token"].asString
    json.remove("token")

    val challenge = json["challenge"].asString.removeSuffix("==")

    return PasskeyJson(
        type = type,
        challenge = challenge,
        req = json.toString(),
        token = token
    )
}

fun String.toCreatePasskeyUserReq(
    email: String,
    token: String,
    countryCode: String
) =
    CreatePasskeyUserReq(
        id = this,
        email = email,
        userName = email.removeSuffix("@gmail.com"),
        token = token,
        countryCode = countryCode
    )


fun String.toGetPasskeyUserReq(token: String) = GetPasskeyUserReq(
    id = this,
    token = token
)


fun String.toGoogleAuthReq(countryCode: String): GoogleAuthReq = GoogleAuthReq(
    type = TYPE_GOOGLE_AUTH_REQ,
    authType = AUTH_TYPE_GOOGLE,
    tokenId = this,
    countryCode = countryCode
)

fun EmailSignUpState.toEmailSignUpReq(countryCode: String) = EmailSignUpReq(
    type = TYPE_EMAIL_SIGN_UP_REQ,
    authType = AUTH_TYPE_EMAIL_SIGN_UP,
    email = this.email,
    password = this.password,
    userName = this.userName,
    countryCode = countryCode
)

fun EmailLogInState.toEmailLogInReq() = EmailLogInReq(
    type = TYPE_EMAIL_LOG_IN_REQ,
    authType = AUTH_TYPE_EMAIL_LOG_IN,
    email = this.email,
    password = this.password
)

fun EmailLogInState.toEmailLogInReq(email: String, password: String) = EmailLogInReq(
    type = TYPE_EMAIL_LOG_IN_REQ,
    authType = AUTH_TYPE_EMAIL_LOG_IN,
    email = email,
    password = password
)

fun ResponseSong.toSongTable() = SongTable(
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    totalTime = this.totalTime,
    title = this.title,
    artist = this.artist,
    album = this.album,
    genre = this.genre,
    composer = this.composer,
    publisher = this.publisher,
    albumArtist = this.albumArtist,
    description = this.description,
    track = this.track,
    date = this.date
)

fun Iterable<PlaylistWithSongs>.toListOfUiPlaylist(): List<UiPlaylist> {
    val map = HashMap<String, ArrayList<SongInfo>>()

    this.forEach {
        if (!map.containsKey(it.song.name)) map[it.song.name] = arrayListOf(it.song.song)
        else if (map.containsKey(it.song.name)) map[it.song.name]?.add(it.song.song)
    }

    return map.map {
        UiPlaylist(
            name = it.key,
            songs = it.value
        )
    }
}


fun SuggestGenreResponse.toUiGenreList(): List<UiGenre> = this.genreList.map {
    UiGenre(
        name = it,
        isSelected = false
    )
}

fun Iterable<UiGenre>.toGenreNameList(): List<String> = this.mapNotNull {
    if (it.isSelected) it.name else null
}

fun List<String>.toStoreGenreReq() = StoreGenreReq(
    data = this
)

fun Iterable<UiGenre>.toAlreadySendGenreList(): List<String> = this.map { it.name }


fun SuggestArtistResponse.toUiArtistList() = this.artistList.map {
    UiArtist(
        name = it.name,
        profileUrl = it.imageUrl,
        isSelected = false
    )
}

fun Iterable<UiArtist>.toAlreadySendArtistList() = this.map {
    it.name
}


fun Iterable<UiArtist>.toArtistNameList() = this.mapNotNull {
    if (it.isSelected) it.name else null
}

fun List<String>.toStoreArtistReq() = StoreArtistReq(
    data = this
)


fun AlbumPreview.toAlbumTablePrevEntry() = AlbumPrevTable(
    name = this.name
)

fun FevArtistsMixPreview.toFevArtistMixPrevTable(
    context: Context,
    isCookie: Boolean,
    header: String,
) = FevArtistsMixPreviewTable(
    artist = this.artist,
    coverImage = this.coverImage.encodeImage(
        context,
        isCookie,
        header
    )
)

fun ResponseArtist.toArtistTableEntry(
    context: Context,
    isCookie: Boolean,
    header: String,
) = ArtistPrevTable(
    id = this.id,
    name = this.name,
    imageUrl = this.imageUrl.encodeImage(
        context,
        isCookie,
        header
    )
)

fun SongPreview.toSongPrevTableEntry(
    context: Context,
    isCookie: Boolean,
    header: String,
) = SongPreviewTable(
    songId = this.id.toLong(),
    title = this.title,
    coverImage = this.coverImage.encodeImage(
        context,
        isCookie,
        header
    ),
    artist = this.artist,
    album = this.album
)


fun AlbumPrevResult.toSongPrev() = HomeUiSongPrev(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    artist = this.artist
)


fun FevArtistsMixPreviewTable.toHomeUiFevArtistMix() = HomeUiFevArtistMix(
    id = this.id,
    name = this.artist,
    coverImage = this.coverImage
)

fun ArtistPrevResult.toHomeUiSongPrev() = HomeUiSongPrev(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    artist = this.name
)


private fun String.encodeImage(
    context: Context,
    isCookie: Boolean,
    header: String,
): String = runBlocking {
    Log.d("url", this@encodeImage)

    val req = ImageRequest.Builder(context)
        .addHeader(if (isCookie) "Cookie" else "Authorization", header)
        .data(this@encodeImage)
        .build()

    try {
        (ImageLoader(context).execute(req).drawable as BitmapDrawable).bitmap.let {
            BitmapConverter.encodeToSting(it)
        }
    } catch (e: Exception) {
        this@encodeImage
    }
}


