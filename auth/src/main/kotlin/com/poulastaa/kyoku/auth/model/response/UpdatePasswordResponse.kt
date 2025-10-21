package com.poulastaa.kyoku.auth.model.response

data class UpdatePasswordResponse(
    val status: UpdatePasswordStatus = UpdatePasswordStatus.USER_NOT_FOUND,
)
