package com.poulastaa.kyoku.auth.model.response

import com.poulastaa.kyoku.auth.utils.JWTToken

data class CodeValidationResponse(
    val status: CodeValidationResponseStatus = CodeValidationResponseStatus.INVALID_CODE,
    val token: JWTToken = "",
)
