package com.poulastaa.data.model.item

import kotlinx.serialization.Serializable

@Serializable
enum class ItemOperation {
    ADD,
    DELETE,
    ERR
}