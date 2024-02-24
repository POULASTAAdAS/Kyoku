package com.poulastaa.kyoku.utils

import com.google.gson.JsonParser
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.api.req.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.req.EmailLogInReq
import com.poulastaa.kyoku.data.model.api.req.EmailSignUpReq
import com.poulastaa.kyoku.data.model.api.req.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.req.GoogleAuthReq
import com.poulastaa.kyoku.data.model.api.req.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist.ResponseSong
import com.poulastaa.kyoku.data.model.screens.auth.email.login.EmailLogInState
import com.poulastaa.kyoku.data.model.screens.auth.email.signup.EmailSignUpState
import com.poulastaa.kyoku.data.model.screens.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
import com.poulastaa.kyoku.data.model.database.SongInfo
import com.poulastaa.kyoku.data.model.database.table.PlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.SongTable
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

fun <T> Iterable<T>.toListOfSongTable(): List<SongTable> {
    val list = ArrayList<SongTable>()

    this.forEach {
        it as ResponseSong
        list.add(it.toSongTable())
    }

    return list
}

fun toPlaylistTable(name: String) = PlaylistTable(
    name = name,
)

fun playlistRelationTable(songId: Long, playlistId: Long) = PlaylistRelationTable(
    playlistId = playlistId,
    songId = songId
)

fun Iterable<PlaylistWithSongs>.toListOfUiPlaylist(): List<UiPlaylist> {
    val list = ArrayList<UiPlaylist>()

    val map = HashMap<String, ArrayList<SongInfo>>()

    this.forEach {
        if (!map.containsKey(it.song.name)) {
            map[it.song.name] = arrayListOf(it.song.song)
        } else if (map.containsKey(it.song.name)) {
            map[it.song.name]?.add(it.song.song)
        }
    }

    map.forEach {
        list.add(
            UiPlaylist(
                name = it.key,
                songs = it.value
            )
        )
    }

    return list
}