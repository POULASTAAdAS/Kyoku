package com.poulastaa.kyoku.utils

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
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
import com.poulastaa.kyoku.data.model.database.SongInfo
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistTable
import com.poulastaa.kyoku.data.model.database.table.FevArtistsMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongTable
import com.poulastaa.kyoku.data.model.screens.auth.email.login.EmailLogInState
import com.poulastaa.kyoku.data.model.screens.auth.email.signup.EmailSignUpState
import com.poulastaa.kyoku.data.model.screens.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.data.model.ui.UiPlaylist
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_EMAIL_LOG_IN
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_EMAIL_SIGN_UP
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_GOOGLE
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_PASSKEY
import com.poulastaa.kyoku.utils.Constants.TYPE_EMAIL_LOG_IN_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_EMAIL_SIGN_UP_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_GOOGLE_AUTH_REQ
import com.poulastaa.kyoku.utils.Constants.TYPE_PASSKEY_AUTH_REQ

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

fun Iterable<ResponseSong>.toListOfSongTable(): List<SongTable> {
    val list = ArrayList<SongTable>()

    this.forEach {
        list.add(it.toSongTable())
    }

    return list
}

fun playlistRelationTable(songId: Long, playlistId: Long) = SongPlaylistRelationTable(
    playlistId = playlistId,
    songId = songId
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


fun AlbumPreview.toAlbumTableEntry() = AlbumTable(
    name = this.name,
    coverImage = this.coverImage,
    title = this.title,
    artist = this.artist
)

fun FevArtistsMixPreview.toFevArtistMixPrevTable() = FevArtistsMixPreviewTable(
    artist = this.artist,
    coverImage = this.coverImage
)

fun ResponseArtist.toArtistTableEntry() = ArtistTable(
    id = this.id,
    name = this.name,
    imageUrl = this.imageUrl
)

fun SongPreview.toSongPrevTableEntry() = SongPreviewTable(
    songId = this.id.toLong(),
    title = this.title,
    coverImage = this.coverImage,
    artist = this.artist,
    album = this.album
)










