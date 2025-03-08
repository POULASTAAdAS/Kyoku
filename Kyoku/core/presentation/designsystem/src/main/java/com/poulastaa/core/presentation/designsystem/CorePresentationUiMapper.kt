package com.poulastaa.core.presentation.designsystem

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.presentation.designsystem.model.UiDetailedPrevSong
import com.poulastaa.core.presentation.designsystem.model.UiPreSong
import com.poulastaa.core.presentation.designsystem.model.UiUser

fun DtoUser.toUiUser() = UiUser(
    username = this.name.ifEmpty { "User" },
    email = this.email,
    profilePic = this.profilePic
)

fun DtoPrevSong.toUiPrevSong() = UiPreSong(
    id = this.id,
    title = this.title,
    poster = this.poster
)

fun DtoDetailedPrevSong.toUiDetailedPrevSong() = UiDetailedPrevSong(
    id = this.id,
    title = this.title,
    artists = this.artists,
    poster = this.poster
)