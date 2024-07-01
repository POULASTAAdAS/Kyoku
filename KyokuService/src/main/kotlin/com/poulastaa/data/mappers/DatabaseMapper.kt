package com.poulastaa.data.mappers

import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.dao.user.EmailAuthUserDao
import com.poulastaa.data.dao.user.GoogleAuthUserDao
import com.poulastaa.domain.model.ResultArtist
import com.poulastaa.domain.model.ResultSong
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType

fun ArtistDao.toArtistResult() = ResultArtist(
    id = this.id.value,
    name = this.name,
    profilePic = this.profilePic ?: ""
)

fun SongDao.toResultSong() = ResultSong(
    id = this.id.value,
    coverImage = this.constructCoverImage(),
    title = this.title,
    releaseYear = this.year,
    masterPlaylistUrl = this.constructMasterPlaylistUrl()
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