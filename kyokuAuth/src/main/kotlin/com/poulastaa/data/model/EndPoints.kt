package com.poulastaa.data.model

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/auth")

    data object CreatePasskeyUser : EndPoints(route = "/api/auth/createPasskeyUser")
    data object GetPasskeyUser : EndPoints(route = "/api/auth/getPasskeyUser")

    data object VerifyEmail : EndPoints(route = "/api/auth/verifyEmail")
    data object EmailVerificationCheck : EndPoints(route = "/api/auth/emailVerificationCheck")
    data object ResendVerificationMail : EndPoints(route = "/api/auth/resendVerificationMail")

    data object ForgotPassword : EndPoints(route = "/api/auth/forgotPassword")
    data object ResetPassword : EndPoints(route = "/api/auth/resetPassword")

    data object RefreshToken : EndPoints(route = "/api/auth/refreshToken")

    data object ProfilePic : EndPoints(route = "/api/authorised/user/profilePic")

    data object GetArtistImageUrl : EndPoints(route = "/api/authorised/getArtistImage")
    data object CoverImage : EndPoints(route = "/api/authorised/coverImage")

    data object UnAuthorised : EndPoints(route = "/api/unauthorised")
}