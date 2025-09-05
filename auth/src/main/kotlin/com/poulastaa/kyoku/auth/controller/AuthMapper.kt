package com.poulastaa.kyoku.auth.controller

import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.response.ResponseStatus
import com.poulastaa.kyoku.auth.model.response.ResponseUser
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import com.poulastaa.kyoku.auth.model.response.UserStatus
import org.springframework.http.ResponseEntity

fun DtoUser.toResponse(status: ResponseStatus) = ResponseUser(
    email = this.email,
    username = this.username,
    profileUrl = this.profileUrl,
    type = this.type,
    status = when (status) {
        ResponseStatus.USER_CREATED -> UserStatus.USER_CREATED
        ResponseStatus.USER_FOUND -> UserStatus.USER_FOUND
        ResponseStatus.USER_FOUND_NO_PLAYLIST -> UserStatus.USER_FOUND_NO_PLAYLIST
        ResponseStatus.USER_FOUND_NO_ARTIST -> UserStatus.USER_FOUND_NO_ARTIST
        ResponseStatus.USER_FOUND_NO_GENRE -> UserStatus.USER_FOUND_NO_GENRE
        ResponseStatus.USER_FOUND_NO_B_DATE -> UserStatus.USER_FOUND_NO_B_DATE
        else -> UserStatus.USER_NOT_FOUND
    }
)


fun ResponseWrapper<DtoUser>.toSingInUpResponse() = ResponseWrapper(
    status = this.status,
    payload = this.payload?.toResponse(this.status) ?: ResponseUser()
).let { wrapper ->
    ResponseEntity.status(wrapper.status.code).body(wrapper)
}