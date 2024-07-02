package com.poulastaa.data.mappers

import com.poulastaa.data.dao.user.EmailAuthUserDao
import com.poulastaa.data.dao.user.GoogleAuthUserDao
import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.data.model.SongDto
import com.poulastaa.domain.model.PlaylistResult
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType

fun PlaylistResult.toPlaylistDto() = PlaylistDto(
    id = this.id,
    name = this.name,
    listOfSong = this.listOfSong.map {
        SongDto(
            id = it.resultSong.id,
            coverImage = it.resultSong.coverImage,
            title = it.resultSong.title,
            artistName = it.artistList.joinToString { artist -> artist.name },
            releaseYear = it.resultSong.releaseYear,
            masterPlaylistUrl = it.resultSong.masterPlaylistUrl
        )
    }
)

@JvmName("GoogleUserToUserResult")
fun GoogleAuthUserDao.toUserResult() = UserResult(
    id = id.value,
    email = email,
    userName = userName,
    profilePic = profilePicUrl,
    password = sub,
    bDate = bDate ?: -1,
    countryId = countryId,
    userType = UserType.GOOGLE_USER
)

@JvmName("EmailUserToUserResult")
fun EmailAuthUserDao.toUserResult() = UserResult(
    id = id.value,
    email = email,
    userName = userName,
    profilePic = profilePic,
    password = password,
    bDate = bDate ?: -1,
    countryId = countryId,
    userType = UserType.EMAIL_USER
)