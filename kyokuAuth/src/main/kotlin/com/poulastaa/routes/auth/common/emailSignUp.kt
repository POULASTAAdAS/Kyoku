package com.poulastaa.routes.auth.common

import com.poulastaa.data.model.auth.EmailSignUpReq
import com.poulastaa.data.model.auth.jwt.EmailSignInResponse
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.verifyEmailIdWithApi
import io.ktor.http.*


suspend fun handleEmailSignup(
    emailSignUpReq: EmailSignUpReq,
    userService: UserServiceRepository,
    response: suspend (EmailSignInResponse, HttpStatusCode) -> Unit
) {
    if (false) // reduce unnecessary api call while developing
        verifyEmailIdWithApi(
            email = emailSignUpReq.email,
        ).let {
            if (!it) {
                response(
                    EmailSignInResponse(
                        status = UserCreationStatus.EMAIL_NOT_VALID
                    ),
                    HttpStatusCode.BadRequest
                )

                return
            }
        }

    val result = userService.createEmailUser(
        userName = emailSignUpReq.userName.trim(),
        email = emailSignUpReq.email.trim(),
        password = emailSignUpReq.password.trim(),
        countryCode = emailSignUpReq.countryCode
    )

    when (result.status) {
        UserCreationStatus.CREATED -> {
            response(
                result,
                HttpStatusCode.OK
            )
        }

        UserCreationStatus.CONFLICT -> {
            response(
                result,
                HttpStatusCode.OK
            )
        }

        UserCreationStatus.SOMETHING_WENT_WRONG -> {
            response(
                result,
                HttpStatusCode.InternalServerError
            )
        }

        else -> Unit
    }
}