package com.poulastaa.kyoku.auth.controller

import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.response.ResponseUser
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import org.springframework.http.ResponseEntity

fun DtoUser.toResponse() = ResponseUser(
    email = this.email,
    username = this.username,
    profileUrl = this.profileUrl,
)


fun ResponseWrapper<DtoUser>.toSingInUpResponse() = ResponseWrapper(
    status = this.status,
    payload = this.payload?.toResponse() ?: ResponseUser()
).let { wrapper ->
    ResponseEntity.status(wrapper.status.code).body(wrapper)
}