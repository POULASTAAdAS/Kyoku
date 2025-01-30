package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class UpsertOperation {
    INSERT,
    UPDATE,
    DELETE
}

@Serializable
data class UpsertReq<T>(
    val list: List<T>,
    val operation: UpsertOperation,
)
