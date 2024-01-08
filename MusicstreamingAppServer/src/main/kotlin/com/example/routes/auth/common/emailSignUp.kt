package com.example.routes.auth.common

import com.example.data.model.EndPoints
import com.example.data.model.auth.EmailSignInResponse
import com.example.data.model.auth.EmailSignUpReq
import com.example.data.model.auth.UserCreationStatus
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.util.Constants
import com.example.util.Constants.JWT_TOKEN_DEFAULT_TIME
import com.example.util.generateJWTTokenWithClaimMailId
import com.example.util.sendEmail
import com.example.util.verifyEmailIdWithApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*


suspend fun PipelineContext<Unit, ApplicationCall>.handleEmailSignup(
    emailSignUpReq: EmailSignUpReq,
    emailAuthUser: EmailAuthUserRepository,
) {
    if (false) // reduce unnecessary api call while developing
        verifyEmailIdWithApi(
            email = emailSignUpReq.email,
        ).let {
            if (!it) {
                call.respond(
                    message = EmailSignInResponse(
                        status = UserCreationStatus.EMAIL_NOT_VALID
                    ),
                    status = HttpStatusCode.BadRequest
                )
                return
            }
        }

    val result = createUser(
        emailSignUpReq.userName,
        emailSignUpReq.email,
        emailSignUpReq.password,
        emailAuthUser = emailAuthUser,
        env = call.application.environment
    )

    when (result.status) {
        UserCreationStatus.CREATED -> {
            call.respond(
                message = result,
                status = HttpStatusCode.OK
            )
        }

        UserCreationStatus.CONFLICT -> {
            call.respond(
                message = result,
                status = HttpStatusCode.Conflict
            )
            return
        }

        UserCreationStatus.SOMETHING_WENT_WRONG -> {
            call.respond(
                message = result,
                status = HttpStatusCode.InternalServerError
            )
            return
        }

        UserCreationStatus.EMAIL_NOT_VALID -> Unit // this will not occur
    }

    sendEmail( // conform email
        emailSignUpReq.email.trim(),
        subject = "Authentication Mail",
        content = (
                (
                        "<html>"
                                + "<body>"
                                + "<h1>Email Authentication</h1>"
                                + "<p>Click the following link to authenticate your email:</p>"
                                + "<a href=\"${Constants.BASE_URL + EndPoints.VerifyEmail.route}?token=" + emailSignUpReq.email.trim()
                            .generateJWTTokenWithClaimMailId(call.application.environment)
                        ) + "\">Authenticate</a>"
                        + "</body>"
                        + "</html>"
                )
    )
}


private suspend fun createUser(
    userName: String,
    email: String,
    password: String,
    emailAuthUser: EmailAuthUserRepository,
    env: ApplicationEnvironment,
): EmailSignInResponse {
    return emailAuthUser.createUser(
        userName = userName.trim(),
        email = email.trim(),
        password = password.trim(),
        token = email.trim().generateJWTTokenWithClaimMailId(
            env = env,
            time = JWT_TOKEN_DEFAULT_TIME
        )
    )
}