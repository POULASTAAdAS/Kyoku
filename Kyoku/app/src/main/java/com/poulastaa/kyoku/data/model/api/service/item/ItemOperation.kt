package com.poulastaa.kyoku.data.model.api.service.item

import kotlinx.serialization.Serializable

@Serializable
enum class ItemOperation {
    ADD,
    DELETE,
    ERR
}