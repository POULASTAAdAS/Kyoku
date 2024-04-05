package com.poulastaa.data.model.pinned

import kotlinx.serialization.Serializable

@Serializable
enum class PinnedOperation {
    ADD,
    REMOVE,
    ERR
}