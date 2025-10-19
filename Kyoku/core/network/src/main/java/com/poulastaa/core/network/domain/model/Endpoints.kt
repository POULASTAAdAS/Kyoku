package com.poulastaa.core.network.domain.model

sealed class Endpoints(val endpoint: String) {
    data object EmailSingIn : Endpoints("/api/v1/auth/email/login")
    data object EmailSingUp : Endpoints("/api/v1/auth/email/create-account")

}