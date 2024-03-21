package com.poulastaa.utils

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.auth_response.HomeResponse
import com.poulastaa.data.model.auth.auth_response.ResponseSong
import com.poulastaa.data.model.auth.google.GoogleAuthResponse
import com.poulastaa.data.model.auth.google.Payload
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.data.model.db_table.PlaylistResult
import com.poulastaa.data.model.db_table.PlaylistTable
import com.poulastaa.data.model.db_table.song.SongTable
import com.poulastaa.domain.dao.user.GoogleAuthUser
import com.poulastaa.domain.dao.user.PasskeyAuthUser
import com.poulastaa.utils.Constants.PROFILE_PIC_URL
import org.jetbrains.exposed.sql.ResultRow


fun GoogleAuthUser.toGoogleAuthResponse(
    status: UserCreationStatus,
    homeResponse: HomeResponse = HomeResponse()
): GoogleAuthResponse =
    GoogleAuthResponse(
        status = status,
        user = User(
            userName = this.userName,
            profilePic = this.profilePicUrl
        ),
        data = homeResponse
    )

fun PasskeyAuthUser.toPasskeyAuthResponse(
    status: UserCreationStatus,
    homeResponse: HomeResponse = HomeResponse()
): PasskeyAuthResponse = PasskeyAuthResponse(
    status = status,
    user = User(
        userName = this.displayName,
        profilePic = PROFILE_PIC_URL
    ),
    data = homeResponse
)


fun GoogleIdToken.toPayload() = Payload(
    sub = this.payload["sub"].toString(),
    userName = this.payload["name"].toString(),
    email = this.payload["email"].toString(),
    pictureUrl = this.payload["picture"].toString()
)


fun ResultRow.toResponseSong(): ResponseSong = ResponseSong(
    coverImage = this[SongTable.coverImage].constructCoverPhotoUrl(),
    masterPlaylistUrl = this[SongTable.masterPlaylistPath].constructMasterPlaylistUrl(),
    totalTime = this[SongTable.totalTime],
    title = this[SongTable.title],
    artist = this[SongTable.artist],
    album = this[SongTable.album],
    genre = this[SongTable.genre],
    composer = this[SongTable.composer],
    publisher = this[SongTable.publisher],
    albumArtist = this[SongTable.album_artist],
    description = this[SongTable.description],
    track = this[SongTable.track],
    date = this[SongTable.date]
)

fun ResultRow.toPlaylistResult() = PlaylistResult(
    playlistId = this[PlaylistTable.id].value,
    playlistName = this[PlaylistTable.name],
    coverImage = this[SongTable.coverImage].constructCoverPhotoUrl(),
    masterPlaylistUrl = this[SongTable.masterPlaylistPath].constructMasterPlaylistUrl(),
    totalTime = this[SongTable.totalTime],
    title = this[SongTable.title],
    artist = this[SongTable.artist],
    album = this[SongTable.album],
    genre = this[SongTable.genre],
    composer = this[SongTable.composer],
    publisher = this[SongTable.publisher],
    albumArtist = this[SongTable.album_artist],
    description = this[SongTable.description],
    track = this[SongTable.track],
    date = this[SongTable.date]
)

fun PlaylistResult.toResponseSong() = ResponseSong(
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
