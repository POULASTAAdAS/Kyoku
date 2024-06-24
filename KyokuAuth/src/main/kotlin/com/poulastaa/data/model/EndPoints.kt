package com.poulastaa.data.model

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/auth")

    data object VerifySignUpEmail : EndPoints(route = "/api/auth/verifySignUpEmail")
    data object VerifyLogInEmail : EndPoints(route = "/api/auth/verifyLogInEmail")

    data object SignUpEmailVerificationCheck : EndPoints(route = "/api/auth/signUpEmailVerificationCheck")
    data object LogInEmailVerificationCheck : EndPoints(route = "/api/auth/logInEmailVerificationCheck")

    data object ForgotPassword : EndPoints(route = "/api/auth/forgotPassword")
    data object ResetPassword : EndPoints(route = "/api/auth/resetPassword")

    data object RefreshToken : EndPoints(route = "/api/auth/refreshToken")

    data object ProfilePic : EndPoints(route = "/api/authorised/user/profilePic")

    data object UnAuthorised : EndPoints(route = "/api/unauthorised")
}