package com.poulastaa.auth.data.mapper

import com.poulastaa.auth.data.model.res.EmailAuthDto
import com.poulastaa.auth.data.model.res.GoogleAuthDto
import com.poulastaa.auth.data.model.res.ResponseUserDto
import com.poulastaa.auth.data.model.res.UserAuthStatusDto
import com.poulastaa.auth.domain.auth.EmailAuth
import com.poulastaa.auth.domain.auth.GoogleAuth
import com.poulastaa.auth.domain.auth.ResponseUser
import com.poulastaa.auth.domain.auth.UserAuthStatus
import com.poulastaa.core.domain.User

fun ResponseUserDto.toUser() = User(
    name = this.userName,
    email = this.email,
    profilePic = this.profilePic
)

fun UserAuthStatusDto.toUserAuthStatus() = when (this) {
    UserAuthStatusDto.CREATED -> UserAuthStatus.CREATED

    UserAuthStatusDto.USER_FOUND_HOME -> UserAuthStatus.USER_FOUND_HOME
    UserAuthStatusDto.USER_FOUND_STORE_B_DATE -> UserAuthStatus.USER_FOUND_STORE_B_DATE
    UserAuthStatusDto.USER_FOUND_SET_GENRE -> UserAuthStatus.USER_FOUND_SET_GENRE
    UserAuthStatusDto.USER_FOUND_SET_ARTIST -> UserAuthStatus.USER_FOUND_SET_ARTIST

    UserAuthStatusDto.PASSWORD_DOES_NOT_MATCH -> UserAuthStatus.PASSWORD_DOES_NOT_MATCH
    UserAuthStatusDto.TOKEN_NOT_VALID -> UserAuthStatus.TOKEN_NOT_VALID
    UserAuthStatusDto.USER_NOT_FOUND -> UserAuthStatus.USER_NOT_FOUND
    UserAuthStatusDto.EMAIL_NOT_VALID -> UserAuthStatus.EMAIL_NOT_VALID
    UserAuthStatusDto.EMAIL_NOT_VERIFIED -> UserAuthStatus.EMAIL_NOT_VERIFIED
    UserAuthStatusDto.SOMETHING_WENT_WRONG -> UserAuthStatus.SOMETHING_WENT_WRONG
}
