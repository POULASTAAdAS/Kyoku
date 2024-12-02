package com.poulastaa.core.domain.model

sealed class Endpoints(val route: String) {
    data object Auth : Endpoints(route = "/api/v1/auth")
}