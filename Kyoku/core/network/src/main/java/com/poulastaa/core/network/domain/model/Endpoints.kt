package com.poulastaa.core.network.domain.model

sealed class Endpoints(
    val endpoint: String,
) {
    companion object {
        private const val VERSION_V1: String = "/api/v1"
    }

    data object EmailSingIn : Endpoints("$VERSION_V1/auth/email/login")
    data object EmailSingUp : Endpoints("$VERSION_V1/auth/email/create-account")
    data object CheckVerificationMailStatus : Endpoints("$VERSION_V1/auth/email/verify-email/state")

}