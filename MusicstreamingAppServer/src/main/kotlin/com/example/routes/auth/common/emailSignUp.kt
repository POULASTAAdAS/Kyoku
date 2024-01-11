package com.example.routes.auth.common

import com.example.data.model.auth.req.EmailSignUpReq
import com.example.data.model.auth.res.EmailSignInResponse
import com.example.data.model.auth.stat.UserCreationStatus
import com.example.domain.repository.UserServiceRepository
import com.example.util.verifyEmailIdWithApi
import io.ktor.http.*


// http://127.0.0.1:8080/.well-known/jwks.json

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

    val result = createUser(
        emailSignUpReq.userName,
        emailSignUpReq.email,
        emailSignUpReq.password,
        userService = userService
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
                HttpStatusCode.Conflict
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


private suspend fun createUser(
    userName: String,
    email: String,
    password: String,
    userService: UserServiceRepository,
): EmailSignInResponse {
    return userService.createUser(
        userName = userName.trim(),
        email = email.trim(),
        password = password.trim()
    )
}