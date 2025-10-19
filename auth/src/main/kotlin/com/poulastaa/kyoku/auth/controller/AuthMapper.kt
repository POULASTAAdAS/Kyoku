package com.poulastaa.kyoku.auth.controller

import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.response.ResponseUser
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import com.poulastaa.kyoku.auth.model.response.ResponseStatus
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
        ResponseStatus.EMAIL_NOT_VALID -> UserStatus.INVALID_EMAIL
        ResponseStatus.USER_NOT_FOUND -> UserStatus.USER_NOT_FOUND
        ResponseStatus.PASSWORD_DOES_NOT_MATCH -> UserStatus.PASSWORD_DOES_NOT_MATCH
        ResponseStatus.EMAIL_ALREADY_IN_USE -> UserStatus.EMAIL_ALREADY_IN_USE
        ResponseStatus.INTERNAL_SERVER_ERROR -> UserStatus.INTERNAL_SERVER_ERROR
        else -> UserStatus.USER_NOT_FOUND
    }
)


fun ResponseWrapper<DtoUser>.toSingInUpResponse() = ResponseWrapper(
    status = this.status,
    payload = this.payload?.toResponse(this.status) ?: ResponseUser(
        status = when (this.status) {
            ResponseStatus.USER_CREATED -> UserStatus.USER_CREATED
            ResponseStatus.USER_FOUND -> UserStatus.USER_FOUND
            ResponseStatus.USER_FOUND_NO_PLAYLIST -> UserStatus.USER_FOUND_NO_PLAYLIST
            ResponseStatus.USER_FOUND_NO_ARTIST -> UserStatus.USER_FOUND_NO_ARTIST
            ResponseStatus.USER_FOUND_NO_GENRE -> UserStatus.USER_FOUND_NO_GENRE
            ResponseStatus.USER_FOUND_NO_B_DATE -> UserStatus.USER_FOUND_NO_B_DATE
            ResponseStatus.EMAIL_NOT_VALID -> UserStatus.INVALID_EMAIL
            ResponseStatus.PASSWORD_DOES_NOT_MATCH -> UserStatus.PASSWORD_DOES_NOT_MATCH
            ResponseStatus.USER_NOT_FOUND -> UserStatus.USER_NOT_FOUND
            ResponseStatus.UNAUTHORIZED -> UserStatus.USER_NOT_FOUND
            ResponseStatus.INTERNAL_SERVER_ERROR -> UserStatus.INTERNAL_SERVER_ERROR
            ResponseStatus.EMAIL_ALREADY_IN_USE -> UserStatus.EMAIL_ALREADY_IN_USE
        }
    )
).let { wrapper ->
    ResponseEntity.status(wrapper.status.code).body(wrapper)
}