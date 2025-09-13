package com.poulastaa.kyoku.gateway.model

data class ServiceConfigPayload(
    val id: String,
    val uri: String,
    private val servicePath: String,
) {
    val path = servicePath.removePrefix("Path=").removeSuffix("**")
}
