package com.poulastaa.utils

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.poulastaa.data.model.User
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.auth_response.HomeResponse
import com.poulastaa.data.model.auth.auth_response.ResponseSong
import com.poulastaa.data.model.auth.auth_response.SongPreview
import com.poulastaa.data.model.auth.google.GoogleAuthResponse
import com.poulastaa.data.model.auth.google.Payload
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.data.model.db_table.AlbumResponse
import com.poulastaa.data.model.db_table.PlaylistResult
import com.poulastaa.data.model.db_table.PlaylistTable
import com.poulastaa.data.model.db_table.song.SongTable
import com.poulastaa.data.model.db_table.user_album.EmailUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.GoogleUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.PasskeyUserAlbumRelation
import com.poulastaa.data.model.utils.AlbumResult
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


fun ResultRow.toAlbumResponse(userType: UserType) = AlbumResponse(
    id = this[
        when (userType) {
            UserType.GOOGLE_USER -> GoogleUserAlbumRelation.albumId
            UserType.EMAIL_USER -> EmailUserAlbumRelation.albumId
            UserType.PASSKEY_USER -> PasskeyUserAlbumRelation.albumId
        }
    ],
    name = this[SongTable.album],
    song = this.toResponseSong()
)


fun ResultRow.toResponseSong() = ResponseSong(
    id = this[SongTable.id].value,
    coverImage = this[SongTable.coverImage].constructCoverPhotoUrl(),
    masterPlaylistUrl = this[SongTable.masterPlaylistPath].constructMasterPlaylistUrl(),
    totalTime = this[SongTable.totalTime],
    title = this[SongTable.title],
    artist = this[SongTable.artist],
    album = this[SongTable.album],
    date = this[SongTable.date]
)

fun ResultRow.toPlaylistResult() = PlaylistResult(
    songId = this[SongTable.id].value,
    playlistId = this[PlaylistTable.id].value,
    playlistName = this[PlaylistTable.name],
    coverImage = this[SongTable.coverImage].constructCoverPhotoUrl(),
    masterPlaylistUrl = this[SongTable.masterPlaylistPath].constructMasterPlaylistUrl(),
    totalTime = this[SongTable.totalTime],
    title = this[SongTable.title],
    artist = this[SongTable.artist],
    album = this[SongTable.album],
    date = this[SongTable.date]
)

fun PlaylistResult.toResponseSong() = ResponseSong(
    id = this.songId,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    totalTime = this.totalTime,
    title = this.title,
    artist = this.artist,
    album = this.album,
    date = this.date
)


fun Iterable<AlbumResult>.toPreviewSong() = this.map {
    it.toPreviewSong()
}

fun AlbumResult.toPreviewSong() = SongPreview(
    id = this.songId.toString(),
    title = this.title,
    artist = this.artist,
    coverImage = this.cover,
    points = this.points,
    year = this.year
)
