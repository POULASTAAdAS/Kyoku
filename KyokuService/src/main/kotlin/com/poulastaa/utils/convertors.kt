package com.poulastaa.utils

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.PlaylistRow
import com.poulastaa.data.model.User
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.spotify.ResponseSong
import com.poulastaa.domain.dao.Song
import com.poulastaa.domain.dao.user.EmailAuthUser
import com.poulastaa.domain.dao.user.GoogleAuthUser
import com.poulastaa.domain.dao.user.PasskeyAuthUser
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

fun <T> Iterable<T>.toResponseSongList(): List<ResponseSong> {
    val list = ArrayList<ResponseSong>()
    this.forEach {
        it as Song
        list.add(it.toResponseSong())
    }
    return list
}

fun <T> Iterable<T>.toSongIdList(): List<Long> {
    val list = ArrayList<Long>()

    this.forEach {
        it as Song
        list.add(it.id.value)
    }

    return list
}

fun <T> List<T>.toListOfPlaylistRow(id: String): List<PlaylistRow> {
    val list = ArrayList<PlaylistRow>()
    this.forEach {
        it as Long
        list.add(
            PlaylistRow(
                songId = it,
                userId = id
            )
        )
    }

    return list
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
