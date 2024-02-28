package com.poulastaa.utils

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.PlaylistRow
import com.poulastaa.data.model.User
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.setup.artist.ResponseArtist
import com.poulastaa.data.model.spotify.ResponseSong
import com.poulastaa.domain.dao.Artist
import com.poulastaa.domain.dao.Song
import com.poulastaa.domain.dao.user.EmailAuthUser
import com.poulastaa.domain.dao.user.GoogleAuthUser
import com.poulastaa.domain.dao.user.PasskeyAuthUser
import com.poulastaa.utils.Constants.ARTIST_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.BASE_URL
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.MASTER_PLAYLIST_ROOT_DIR

fun Song.toResponseSong(): ResponseSong = ResponseSong(
    coverImage = this.coverImage.constructCoverPhotoUrl(),
    masterPlaylistUrl = this.masterPlaylistPath.constructMasterPlaylistUrl(),
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

fun Iterable<Song>.toResponseSongList() = this.map {
    it.toResponseSong()
}

fun List<Long>.toListOfPlaylistRow(id: String) = this.map {
    PlaylistRow(
        songId = it,
        userId = id
    )
}


fun String.constructCoverPhotoUrl(): String = "$BASE_URL${EndPoints.CoverImage.route}?coverImage=${
    this.replace(COVER_IMAGE_ROOT_DIR, "")
}"

fun String.constructMasterPlaylistUrl(): String = "$BASE_URL${EndPoints.PlaySongMaster.route}?playlist=${
    this.replace(MASTER_PLAYLIST_ROOT_DIR, "")
}"

fun String.getAlbum(): String {
    val temp = Regex("\"([^\"]+)\"").find(this)

    temp?.let {
        return it.groupValues[1].trim()
    }
    return this.replace(Regex("\\(.*"), "").trim()
}

fun String.removeAlbum(): String =
    this.replace(Regex("\\(.*"), "").trim()

fun Any.toUser(userType: UserType) = when (userType) {
    UserType.GOOGLE_USER -> {
        this as GoogleAuthUser

        User(
            id = this.id.value,
            userId = this.sub
        )
    }

    UserType.EMAIL_USER -> {
        this as EmailAuthUser

        User(
            id = this.id.value,
            userId = this.email
        )
    }

    UserType.PASSKEY_USER -> {
        this as PasskeyAuthUser

        User(
            id = this.id.value,
            userId = this.userId
        )
    }
}

/**
 * adding _ on blank for easier req handling
 *   must be removed on [Endpoints.GetArtistImageUrl.route]
 */
fun Iterable<Artist>.toResponseArtist() = this.map {
    ResponseArtist(
        id = it.id.value,
        name = it.name,
        imageUrl = ResponseArtist.getArtistImageUrl(
            it.profilePicUrl
                .replace(ARTIST_IMAGE_ROOT_DIR, "")
                .removeSuffix("/")
                .replace(" ", "_")
        )
    )
}

























