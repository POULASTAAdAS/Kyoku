package com.poulastaa.kyoku.data.model.api.service.pinned

import kotlinx.serialization.Serializable

@Serializable
enum class PinnedOperation {
    ADD,
    REMOVE,
    ERR
}